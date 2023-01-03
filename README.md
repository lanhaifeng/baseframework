spring boot练习项目,集成一些常用的框架

##### 手动回滚事务
```
    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testTransactional() {
        try {
           
        } catch (Exception e) {
            log.error("新增失败错误：" + ExceptionUtils.getFullStackTrace(e));
            //通常情况下，主动回滚事务，可以手动抛异常即可，不抛异常可以如下方式回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
```