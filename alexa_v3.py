'''
Created on Dec 18, 2017

@author: shijian
@email: shijian@163.com
'''
# 指定编码
# -*- encoding: UTF-8 -*-

# 导入库
from datetime import datetime, timezone, timedelta
from time import time
from json import loads, dumps
from urllib import request, error

import boto3

# 日志开关
log_flag = True
# 服务器地址
url = 'http://127.0.0.1:8080/api/alexa/v3'


# Lambda服务主函数
def lambda_handler(event, context):
    # event: Alexa传递请求参数
    namespace = get_map('directive\nheader\nnamespace', event)

    # 定义日志主键
    log_id = None
    if log_flag == True:
        # 保存日志
        log_id = save_or_update_log(namespace=namespace, req=event)

    result = None
    if namespace == 'Alexa.Discovery':
        # 设备发现
        result = discovery(log_id, namespace, event)
    else:
        # 设备控制
        result = control(log_id, namespace, event)

    if log_flag == True:
        # 更新日志
        save_or_update_log(namespace=namespace, id=log_id, resp=result)

    if result:
        return result


# 日志保存函数, id: 日志主键, event: Alexa请求参数, resp: Lambda响应Alexa参数, server: 服务器响应Lambda参数
# 注意 本地测试如果没有boto3库, 可以将该函数中代码注释成空函数, 并将导入boto3库命令注释
def save_or_update_log(namespace=None, id=None, req=None, resp=None, server=None):
    # 获取client对象, region_name: 数据库所在区域
    client = boto3.resource('dynamodb', region_name='us-east-1')
    # 获取表操作对象
    table = client.Table('rc-log')
    # 根据ID判断插入还是更新
    if id == None:
        # 当前时间戳
        timestamp = int(time() * 1000)
        id = str(timestamp)
        # 插入JSON数据
        table.put_item(
            Item={
                'id': id,  # 主键
                'sort': timestamp,  # 排序子弹
                'namespace': namespace,  # Alexa请求名称空间
                'name': get_map('directive\nheader\nname', req),  # Alexa请求类型
                'z_req': dumps(req),  # 将Alexa转换字符串格式
                'z_creation_time': datetime.utcnow().replace(tzinfo=timezone.utc).astimezone(
                    timezone(timedelta(hours=8))).strftime('%Y/%m/%d %H:%M:%S')  # 存储东八区格式化时间字符串
            }
        )
    else:
        expression = 'SET '  # 修改字段
        values = {}  # 字段值
        if resp != None:
            expression += 'z_resp=:resp,'
            values[':resp'] = dumps(resp)
        elif server != None:
            expression += 'z_server=:server,'
            values[':server'] = dumps(server)
        expression = expression[:-1]  # 截取
        # 更新JSON数据
        table.update_item(
            Key={
                'id': id,
                'sort': int(id)
            },
            UpdateExpression=expression,
            ExpressionAttributeValues=values,
            ReturnValues='UPDATED_NEW'
        )
    return id


# 获取级联属性值
def get_map(exp, map):
    val = None
    if map:
        keys = exp.split('\n')
        for key in keys:
            if key in map.keys():
                val = map[key]
                map = val
    return val


# 处理远程响应数据
def get_remote_response(message_id, correlation_token, endpoint_id, access_token, resp):
    name = 'Response'
    properties = None
    payload = None
    if 'status' not in resp.keys() or resp['status'] == 0:
        properties = [];
        if 'rows' in resp.keys():
            # 存在数据
            for property in resp['rows']:
                properties.append(property)
    else:
        name = 'ErrorResponse'
        type = 'ENDPOINT_BUSY'
        message = 'The target endpoint cannot respond.'
        status = resp['status']
        # token失效
        if status == 40001:
            type = 'EXPIRED_AUTHORIZATION_CREDENTIAL'
            message = 'The OAuth2 access token for that customer has expired.'
        payload = {
            'type': type,
            'message': message
        }
    result = {
        'event': {
            'header': {
                'namespace': 'Alexa',
                'name': name,
                'payloadVersion': '3',
                'messageId': message_id,
                'correlationToken': correlation_token
            },
            'endpoint': {
                'scope': {
                    'type': 'BearerToken',
                    'token': access_token
                },
                'endpointId': endpoint_id
            },
            'payload': {}
        }
    }
    if properties != None:
        # 成功
        result['context'] = {'properties': properties}
    else:
        # 出现错误
        result['event']['payload'] = payload
    return result


# 发现情景, 设备
def discovery(log_id, namespace, event):
    token = get_map('directive\npayload\nscope\ntoken', event)
    headers = {'Authorization': 'Bearer ' + token};

    resp = send(url + '/discovery', headers)  # 请求服务端获取响应

    if log_flag == True:
        save_or_update_log(namespace=namespace, id=log_id, server=resp)  # 记录日志

    req_header = get_map('directive\nheader', event)  # 获取Alexa请求头
    message_id = req_header['messageId']  # 获取当前命令id

    return get_remote_response(message_id, None, None, token, resp);


# 获取远程控制数据
def get_control_data(namespace, name, endpoint_id, parameter=None):
    data = {
        'namespace': namespace,
        'name': name,
        'endpointId': endpoint_id
    }
    if parameter != None:
        for key in parameter.keys():
            data[key] = parameter[key]
    return data


# 设备控制
def control(log_id, namespace, event):
    req_header = get_map('directive\nheader', event)  # 获取Alexa请求头
    name = req_header['name']  # 请求类型
    endpoint_id = get_map('directive\nendpoint\nendpointId', event)  # 控制ID

    token = get_map('directive\nendpoint\nscope\ntoken', event)
    headers = {'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json;charset=UTF-8'};

    parameter = None;
    if name == 'SetPowerLevel' or name == 'AdjustPowerLevel' or name == 'SetColor' or name == 'SetColorTemperature' or name == 'SetPercentage' or name == 'AdjustPercentage':
        parameter = get_map('directive\npayload', event)
    data = get_control_data(namespace, name, endpoint_id, parameter)

    resp = send(url + '/remote_control', headers, data=dumps(data), method='POST')
    if log_flag == True:
        save_or_update_log(namespace=namespace, id=log_id, server=resp)  # 记录日志

    message_id = req_header['messageId']  # 获取当前命令id
    correlation_token = get_map('correlationToken', req_header)  # 获取相关Token

    return get_remote_response(message_id, correlation_token, endpoint_id, token, resp)


# http请求
def send(url, headers={}, data=None, method='GET'):
    try:
        if data:
            # 存在数据, UTF-8编码
            data = data.encode('utf-8')
        rep = request.Request(url=url, data=data, headers=headers, method=method)
        resp = request.urlopen(rep)
        respBody = resp.read()
        return loads(respBody.decode('utf-8'))  # JSON解码
    except error.URLError as e:
        return {'status': -1, 'msg': e}
'''
# 设备发现, 本地测试
e = {
    'directive': {
        'header': {
            'namespace': 'Alexa.Discovery',
            'name': 'Discover',
            'payloadVersion': '3',
            'messageId': '1bd5d003-31b9-476f-ad03-71d471922820'
        },
        'payload': {
            'scope': {
                'type': 'BearerToken',
                'token': 'access-token-from-skill'
            }
        }
    }
}
print(lambda_handler(e, None))
'''
