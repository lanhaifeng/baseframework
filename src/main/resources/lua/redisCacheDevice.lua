local in_table=function(value, tab)
  for k,v in ipairs(tab) do
    if v == value then
      return true
    end
  end
  return false
end

local string2Table=function(cacheStr, fc)
    local cacheData={}
    if cacheStr == nil or type(cacheStr) ~= "string" then
    else
    	if fc == nil or type(fc) ~= "string" then
    		fc = "\'|\'"
		end
		local pattern = "([^"..fc.."]+)"
		for w in string.gmatch(cacheStr, pattern) do
	        table.insert(cacheData,w)
	    end
    end
    return cacheData
end

local cacheStr = redis.call('GET', KEYS[1])

if cacheStr == nil or type(cacheStr) ~= "string" then
	redis.call('SET', KEYS[1], ARGV[1])
	return nil
end

local cacheData=string2Table(cacheStr, "|")
local newData=string2Table(ARGV[1], "|")

local cacheCustId=string2Table(cacheData[1], "|")
local cachePlatform=string2Table(cacheData[2], "|")
local cacheLabel=string2Table(cacheData[3], "|")
local cacheCreateTime=cacheData[4];

local newCustId=string2Table(newData[1], "|")
local newPlatform=string2Table(newData[2], "|")
local newLabel=string2Table(newData[3], "|")
local newCreateTime=cacheData[4];


for k,v in ipairs(newCustId) do
    if in_table(v, cacheCustId) then
    else
    	table.insert(cacheCustId,v)
    end
end

for k,v in ipairs(newPlatform) do
    if in_table(v, cachePlatform) then
    else
    	table.insert(cachePlatform,v)
    end
end

for k,v in ipairs(newLabel) do
    if in_table(v, cacheLabel) then
    else
    	table.insert(cacheLabel,v)
    end
end

if cacheCreateTime < newCreateTime then
	cacheCreateTime = newCreateTime
end

local result = table.concat(cacheCustId,",").."|"..table.concat(cachePlatform,",").."|"..table.concat(cacheLabel,",").."|"..cacheCreateTime
redis.call('SET', KEYS[1], result)
return cacheStr