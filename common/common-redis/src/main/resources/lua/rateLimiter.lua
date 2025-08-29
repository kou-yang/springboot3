-- 滑动窗口限流器
-- key
local key = KEYS[1]
-- uuid
local uuid = KEYS[2]
-- 当前时间戳（毫秒）
local now = tonumber(KEYS[3])
-- 滑动窗口大小
local interval = tonumber(ARGV[1])
-- key 过期时间
local seconds = tonumber(ARGV[2])
-- 窗口时间内允许最大访问次数
local maxInInterval = tonumber(ARGV[3])

-- 1 删除集合中距离当前时间超过interval的数据
redis.call('ZREMRANGEBYSCORE', key, 0, now - interval)
-- 2 获取集合中元素的数量
local count = redis.call('ZCARD', key)

-- 3 判断 count 是否大于 maxInInterval
if count >= maxInInterval then
    -- 设置过期时间
    redis.call('EXPIRE', key, math.ceil(seconds))
    -- 请求数量大于阈值 返回 -1
    return -1
end
-- 4 将当前请求存入集合
redis.call('ZADD', key, now, uuid)
-- 设置过期时间
redis.call('EXPIRE', key, math.ceil(seconds))
-- 返回成功
return 1