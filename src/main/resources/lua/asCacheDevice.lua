local function in_table(value, tab)
    for k,v in ipairs(tab) do
        if v == value then
            return true
        end
    end
    return false
end

local function string2Table(cacheStr, fc)
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

function getThenUpdate(rec, bin, newStr)
    if aerospike:exists(rec) then
        local cacheStr=rec[bin]

        local cacheData=string2Table(cacheStr, "|")
        local newData=string2Table(newStr, "|")

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
        rec[bin]=result

        aerospike:update(rec)

        return cacheStr
    else
        rec[bin]=newStr
        aerospike:create(rec)
    end
    return nil
end