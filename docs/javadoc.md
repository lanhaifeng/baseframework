# 文档标注
Javadoc用于描述类或者方法的作用。Javadoc可以写在类上面和方法上面。

## 文档标记
在注释中出现以@开头东东被称之为Javadoc文档标记，是JDK定义好的，如@author、@version、@since、@see、@link、@code、@param、@return、@exception、@throws等

### @link
>@link的使用语法{@link 包名.类名#方法名(参数类型)}，其中当包名在当前类中已经导入了包名可以省略，可以只是一个类名，也可以是仅仅是一个方法名，也可以是类名.方法名，使用此文档标记的类或者方法，可用通过按住Ctrl键+单击 可以快速跳到相应的类或者方法上，解析成html其实就是使用< code> 包名.类名#方法名(参数类型)< /code>
```text
// 完全限定的类名
{@link java.lang.Character}

// 省略包名
{@link String}

// 省略类名，表示指向当前的某个方法
{@link #length()}

// 包名.类名.方法名(参数类型)
{@link java.lang.String#charAt(int)}
```

### @code
>{@code text} 会被解析成<code> text </code>
将文本标记为代码样式的文本，在code内部可以使用 < 、> 等不会被解释成html标签, code标签有自己的样式

>一般在Javadoc中只要涉及到类名或者方法名，都需要使用@code进行标记。

### @param
>一般类中支持泛型时会通过@param来解释泛型的类型
>后面跟参数名，再跟参数描述
```text
/**
* @param str the {@code CharSequence} to check (may be {@code null})
*/
public static boolean containsWhitespace(@Nullable CharSequence str) {}

```

### @author
>详细描述后面一般使用@author来标记作者，如果一个文件有多个作者来维护就标记多个@author，@author 后面可以跟作者姓名(也可以附带邮箱地址)、组织名称(也可以附带组织官网地址)
```text
// 纯文本作者
@author Rod Johnson

// 纯文本作者，邮件
@author Igor Hersht, igorh@ca.ibm.com

// 超链接邮件 纯文本作者
@author <a href="mailto:ovidiu@cup.hp.com">Ovidiu Predescu</a>

// 纯文本邮件
@author shane_curcuru@us.ibm.com

// 纯文本 组织
@author Apache Software Foundation

// 超链接组织地址 纯文本组织
@author <a href="https://jakarta.apache.org/turbine"> Apache Jakarta Turbine</a>
```

### @see
>一般用于标记该类相关联的类,@see即可以用在类上，也可以用在方法上
```text
/**
 * @see IntStream
 * @see LongStream
 * @see DoubleStream
 * @see <a href="package-summary.html">java.util.stream</a>
 * /
public interface Stream<T> extends BaseStream<T, Stream<T>> {}
```

### @since
>一般用于标记文件创建时项目当时对应的版本，一般后面跟版本号，也可以跟是一个时间，表示文件当前创建的时间
```text
package java.util.stream;

/**
* @since 1.8
*/
public interface Stream<T> extends BaseStream<T, Stream<T>> {}

package org.springframework.util;

/**
* @since 16 April 2001
*/
public abstract class StringUtils {}
```

### @version
>version 用于标记当前版本，默认为1.0
```text
package com.sun.org.apache.xml.internal.resolver;
 /**
 * @version 1.0
 */
public class Resolver extends Catalog {}
```

### @return
>跟返回值的描述
```text
/**
* @return {@code true} if the {@code String} is not {@code null}, its
*/
public static boolean hasText(@Nullable String str){}
```

### @throws
跟异常类型 异常描述 , 用于描述方法内部可能抛出的异常
```text
/**
* @throws IllegalArgumentException when the given source contains invalid encoded sequences
*/
public static String uriDecode(String source, Charset charset){}
```

### @exception
> 用于描述方法签名throws对应的异常
```text
/**
* @exception IllegalArgumentException if <code>key</code> is null.
*/
public static Object get(String key) throws IllegalArgumentException {}
```

### @value
> 用于标注在常量上，{@value} 用于表示常量的值
```text
/** 默认数量 {@value} */
private static final Integer QUANTITY = 1;
```

### @inheritDoc
> 用于注解在重写方法或者子类上，用于继承父类中的Javadoc
+ 基类的文档注释被继承到了子类
+ 子类可以再加入自己的注释（特殊化扩展）
+ @return @param @throws 也会被继承


## 类
类上的文档标注一般分为三段：

+ 第一段：概要描述，通常用一句或者一段话简要描述该类的作用，以英文句号作为结束
+ 第二段：详细描述，通常用一段或者多段话来详细描述该类的作用，一般每段话都以英文句号作为结束
  + 详细描述一般用一段或者几个锻炼来详细描述类的作用，详细描述中可以使用html标签，如`<p>、<pre>、<a>、<ul>、<i>`等标签， 通常详细描述都以段落p标签开始。
  + 详细描述和概要描述中间通常有一个空行来分割
+ 第三段：文档标注，用于标注作者、创建时间、参阅类等信息

>一般段落都用p标签来标记，凡涉及到类名和方法名都用@code标记，凡涉及到组织的，一般用a标签提供出来链接地址。

示例
```text
package org.springframework.util;

/**
 * Miscellaneous {@link String} utility methods.
 *
 * <p>Mainly for internal use within the framework; consider
 * <a href="http://commons.apache.org/proper/commons-lang/">Apache's Commons Lang</a>
 * for a more comprehensive suite of {@code String} utilities.
 *
 * <p>This class delivers some simple functionality that should really be
 * provided by the core Java {@link String} and {@link StringBuilder}
 * classes. It also provides easy-to-use methods to convert between
 * delimited strings, such as CSV strings, and collections and arrays.
 *
 */
public abstract class StringUtils {
}
```

## 方法
写在方法上的文档标注一般分为三段：

+ 第一段：概要描述，通常用一句或者一段话简要描述该方法的作用，以英文句号作为结束
+ 第二段：详细描述，通常用一段或者多段话来详细描述该方法的作用，一般每段话都以英文句号作为结束
+ 第三段：文档标注，用于标注参数、返回值、异常、参阅等

>方法详细描述上经常使用html标签来，通常都以p标签开始，而且p标签通常都是单标签，不使用结束标签，其中使用最多的就是p标签和pre标签,ul标签, i标签。

>pre元素可定义预格式化的文本。被包围在pre元素中的文本通常会保留空格和换行符。而文本也会呈现为等宽字体，pre标签的一个常见应用就是用来表示计算机的源代码。

>一般p经常结合pre使用，或者pre结合@code共同使用(推荐@code方式)
>一般经常使用pre来举例如何使用方法

>注意：pre>标签中如果有小于号、大于号、例如泛型 在生产javadoc时会报错

示例
```text
/**
 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
 * <p>More specifically, this method returns {@code true} if the
 * {@code CharSequence} is not {@code null}, its length is greater than
 * 0, and it contains at least one non-whitespace character.
 * <p><pre class="code">
 * StringUtils.hasText(null) = false
 * StringUtils.hasText("") = false
 * StringUtils.hasText(" ") = false
 * StringUtils.hasText("12345") = true
 * StringUtils.hasText(" 12345 ") = true
 * </pre>
 * @param str the {@code CharSequence} to check (may be {@code null})
 * @return {@code true} if the {@code CharSequence} is not {@code null},
 * its length is greater than 0, and it does not contain whitespace only
 * @see Character#isWhitespace
 */
public static boolean hasText(@Nullable CharSequence str) {
	return (str != null && str.length() > 0 && containsText(str));
}
```


# 官网
+ [javadoc](https://docs.oracle.com/javase/1.5.0/docs/tooldocs/solaris/javadoc.html)