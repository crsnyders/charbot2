package za.co.chris.wug.interfaces;

import java.util.List;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.exception.ProviderException;

public interface Provider<D> {

	List<D> process(CommandObject command)throws ProviderException;
}
