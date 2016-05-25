package za.co.chris.wug.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Processor;

@Component("magic")
public class Magic implements Processor{


	private final  ExpressionParser parser = new SpelExpressionParser();
	private Expression exp;
	private final EvaluationContext context;

	@Autowired
	private ApplicationContext applicationContext;

	public Magic() {
		this.context = new StandardEvaluationContext(this.applicationContext);
	}

	@Override
	public boolean canHandle(String key) {
		return key.equalsIgnoreCase("magic");
	}

	@Override
	public Object processCommand(CommandObject command,String requestFrom) {
		this.exp = this.parser.parseExpression(command.payload.get(0));
		return this.exp.getValue(this.context, String.class);
	}

}
