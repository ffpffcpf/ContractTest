# ContractTest
use Moscow and spring-test-dbunit to make contract test easily

## Help you use this Step by Step!

### 1.Dependencies Information
<pre><code>
&lt;dependency&gt;
    &lt;groupId&gt;com.github.macdao&lt;/groupId&gt;
    &lt;artifactId&gt;moscow&lt;/artifactId&gt;
    &lt;version&gt;0.3.0&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
    &lt;groupId&gt;com.squareup.okhttp3&lt;/groupId&gt;
    &lt;artifactId&gt;okhttp&lt;/artifactId&gt;
    &lt;version&gt;3.1.2&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
    &lt;groupId&gt;com.h2database&lt;/groupId&gt;
    &lt;artifactId&gt;h2&lt;/artifactId&gt;
    &lt;version&gt;1.4.196&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
    &lt;groupId&gt;com.github.springtestdbunit&lt;/groupId&gt;
    &lt;artifactId&gt;spring-test-dbunit&lt;/artifactId&gt;
    &lt;version&gt;1.3.0&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
    &lt;groupId&gt;org.dbunit&lt;/groupId&gt;
    &lt;artifactId&gt;dbunit&lt;/artifactId&gt;
    &lt;version&gt;2.5.2&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
</code></pre>

Also,spring-framework is necessary.

### 2.Use Configuration Class to extend ContractTest
<pre><code>public TestBaseBean extends ContractTest{  
...  
}
</code></pre>

### 3.Use @ContextConfiguration to register bean in context
<pre><code>@ContextConfiguration(location={"spring.xml","spring-servlet.xml","dataSource.xml"})
public TestBaseBean extends ContractTest{  
 ...  
}
</code></pre>

### 4.write the test method which name is contract description 
<pre><code>@ContextConfiguration(location={"spring.xml","spring-servlet.xml","dataSource.xml"})
public TestBaseBean extends ContractTest{  
 &emsp; @Test  
 &emsp; public void should_return_hello_world(){  
 &emsp; &emsp;assertContract();  
 &emsp;}  
}
</code></pre>

#### Now you finish configuration in java code, then you need to create your contract.

### 1.Create Contract 
Firstly, I'm show you an example below:   
<pre><code>[  
   {
      "description": "should_return_hello_world",
       "request": {
           "uri": "/sayHello",
           "method": "post",
           "json": {
               "name": "Tom",
               "sex" : "Man"
           }    
       },
       "response": {
           "status": 200,
           "json": {
               "name": "Tom",
               "sex": "Man",
               "words": "hello world"
           }
       }     
    }  
]
</code></pre>

The controller code:  
<pre><code>
@RequestMapping(value = "sayHello",method = RequestMethod.POST
@ResponseBody
public Object sayHello(@RequestBody Person person){
    String words = wordsService.getWordsByName(person.getName());//get words from database
    person.setWords(words);
    return person;
}
</code></pre>
The Person code:  
<pre><code>
public class Person{
    private String name;
    private String sex;
    private String words;
    
    //getter and setter
    ...
}
</code></pre>

In the contract, request object describe what and where you send to the server, response object describe what you expect from server response.  
The contract use Moco to describe, you can see the detail in [Moco](https://github.com/dreamhead/moco).

Importantly, you should put your contracts in *src/test/resources/contracts*.


#### In order to setup and teardown database tables using simple annotations as well as checking expected tables contents once a test completes, I use spring test dbunit.

### 1.Because of some reason, you should remove all bean which name is datasource before using.(If there is not datasource bean, skip this step).

### 2.Put init SQL scripts in *src/test/resources/database/h2*, so that the embedded H2 database create table and ready to be used.

### 3.Create dataSet in the same package as the test class under *src/test/resources*
Here is a dataset example:  
<pre><code>
&lt;dataset&gt;
    &lt;person name="Tom" sex="Man" words="hello world" /&gt;
&lt;/dataset&gt;
</code></pre>
### 4.use @DatabaseSetup and @DatabaseTearDown to describe how the tables like before and after testing.
<pre><code>
@Test
@DatabaseSetup("data_insert.xml")
@DataTearDown("data_delete.xml")  
public void should_return_hello_world(){  
    assertContract();  
}  
</code></pre>

The spring test dbunit can also check expected tables contents once test completes, config multiples datasource and write customer loader(default loader is xml loader).You can see more detail in [spring test dbunit](https://github.com/springtestdbunit/spring-test-dbunit)