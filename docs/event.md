### 事件处理机制
#### jdk事件支持



#### spring事件处理   
##### 1.早期事件发布   

  组件要从Spring事件获知自定义域事件中获取通知，那么组件必须实现ApplicationListener接口并覆写onApplicationEvent方法  
  ```java
  @Component
  class OldWayBlogModifiedEventListener implements ApplicationListener<OldWayBlogModifiedEvent> {
      
      @Override
      public void onApplicationEvent(OldWayBlogModifiedEvent event) {
          externalNotificationSender.oldWayBlogModified(event);
      }
  }
  ```
  
  上面的代码工作正常，但是它会针对每一个事件都创建一个新类，从而造成代码瓶颈。
  另外，我们的事件类继承了ApplicationEvent类——Spring应用中的事件基类。
  ```java
    class OldWayBlogModifiedEvent extends ApplicationEvent {
        public OldWayBlogModifiedEvent(Blog blog) {
            super(blog);
        }
        public Blog getBlog() {
            return (Blog)getSource();
        }
    }
  ```
    
##### 2.注释驱动的事件监听器
  Spring 4.2框架值得注意的一点，用注释@EventListener注解任意的Spring组件。
  ```java
    @EventListener
    public void blogModified(BlogModifiedEvent blogModifiedEvent) {
        externalNotificationSender.blogModified(blogModifiedEvent);
    }
  ```
  
  Spring会为事件创建一个ApplicationListener实例，并从方法参数中获取事件的类型。一个类中被事件注释的方法数量没有限制，所有相关的事件句柄都会分组到一个类中。
  
##### 3.有条件的事件处理
  为了使注释@EventListener的功能更强大，Spring 4.2支持用SpEL表达式表达事件类型的方式。假设以下是事件类：
  ```java
    public class BlogModifiedEvent {
        private final Blog blog;
        private final boolean importantChange;
        public BlogModifiedEvent(Blog blog) {
            this(blog, false);
        }
        public BlogModifiedEvent(Blog blog, boolean importantChange) {
            this.blog = blog;
            this.importantChange = importantChange;
        }
        public Blog getBlog() {
            return blog;
        }
        public boolean isImportantChange() {
            return importantChange;
        }
    }
  ```
  要注意，在实际应用中可能不会有本文这样的层次结构的事件。 
  还要注意，用Groovy编写会更加简单。
  
  使用条件参数来阐述事件，重要的变化是：
  ```java
    @EventListener(condition = "#blogModifiedEvent.importantChange")
    public void blogModifiedSpEL(BlogModifiedEvent blogModifiedEvent) {
    
        externalNotificationSender.blogModifiedSpEL(blogModifiedEvent);
    
    }
  ```
  
##### 4.宽松事件类型的层次结构

  Spring 4.2之前的版本，ApplicationEventPublisher只有在ApplicationEvent事件后发布其继承对象的能力。而在Spring 4.2版开始，此接口已经扩展到支持任意对象类型。在这种情况下，对象被封装到PayloadApplicationEvent和通过发送。
  ```java
    //base class with Blog field - no need to extend `ApplicationEvent`
    class BaseBlogEvent {}
    class BlogModifiedEvent extends BaseBlogEvent {}
    //somewhere in the code
    ApplicationEventPublisher publisher = (...);    
    //injected
    //just plain instance of the event
    publisher.publishEvent(new BlogModifiedEvent(blog));
  ```
  这一变化使得发布事件更容易。然而另一方面它可以导致事件跟踪变得更加困难，特别是在大型应用程序中。
  
##### 5.响应发布事件  
  注释@EventListener还有一点需注意，在非空返回类型时，Spring会自动发布返回的事件。
  ```java
    @EventListener
    public BlogModifiedResponseEvent blogModifiedWithResponse(BlogModifiedEvent blogModifiedEvent) {
        externalNotificationSender.blogModifiedWithResponse(blogModifiedEvent);
        return new BlogModifiedResponseEvent(
            blogModifiedEvent.getBlog(), BlogModifiedResponseEvent.Status.OK);
    }
  ```
  
##### 6.异步事件处理
  注释@EventListener还可以与注释@Async进行组合使用，以提供异步事件处理的机制。下面的代码中，指定的事件监听器既不会阻塞主要的代码执行，又不会被其它的监听器处理。
  ```java
    @Async    //Remember to enable asynchronous method execution 
              //in your application with @EnableAsync
    @EventListener
    public void blogAddedAsync(BlogAddedEvent blogAddedEvent) {
        externalNotificationSender.blogAdded(blogAddedEvent);
    }
  ```
  为了使工作能够得到异步执行，通常还需在Spring项目的上下文中使用注释@EnableAsync。
  
  或者xml文件:
  ```xml
    <!-- 开启异步调用@Async注解-->
    <task:annotation-driven executor="myTaskExecutor"/>
    <task:executor id="myTaskExecutor" pool-size="5"/>
  ```

注：
    注释驱动的事件监听器是Spring框架4.2版中引入的新特性，它减少了Spring项目的样板代码，使得代码更加灵活，尤其是在小数量事件的需求时体现更为明显。
    
   (异步事件处理1)[https://blog.csdn.net/yyxyeyue/article/details/105655057]  
   (异步事件处理2)[https://blog.csdn.net/weixin_43848065/article/details/108148126]  
   (事件监听器详解)[https://www.cnblogs.com/ganbo/p/7818488.html]  