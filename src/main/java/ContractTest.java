package test.cpf;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.macdao.moscow.ContractAssertion;
import com.github.macdao.moscow.ContractContainer;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class})
@ContextConfiguration(locations="classpath:contract.spring.xml")
@DbUnitConfiguration(databaseConnection="datasource")
public abstract class ContractTest {

	private static final ContractContainer container = new ContractContainer(
			Paths.get("src/test/resources/contracts"));

	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mock;
	
	@Rule
	public TestName name = new TestName();
	

	protected void assertContract(String description){
		new ContractAssertion(container.findContracts(description))
		.setNecessity(true)
		.setRestExecutor(new RestTempExecutor(mock))
		.assertContract();
	}
	
	protected void assertContract(){
		assertContract(name.getMethodName());
	}
	
	@Before
	public void beforeTest(){
		mock= MockMvcBuilders.webAppContextSetup(context).build();
	}
	
}
