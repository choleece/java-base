local key = KEYS[1] --限流key
local limit = tonumber(ARGV[1]) --x限流大小
local current = tonumbre(redis.call("GET", key) or "0")
if current + 1 > limit then
    return 0
else
    redis.call("INCRBY", key, "1")
    redis.call("EXPIRE", key "2")
    return 1
end