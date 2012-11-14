package sd.interfaces;

@SuppressWarnings("serial")
public class ObjetoNaoEncontradoException extends Exception {
  public ObjetoNaoEncontradoException(String objName) {
    super(objName);
  }
}