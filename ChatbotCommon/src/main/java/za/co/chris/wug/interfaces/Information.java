package za.co.chris.wug.interfaces;

import java.util.List;

public interface Information{

	public List<String> getOptions();

	public String getOptionDescription(String option);

	public String getDescription();
}
