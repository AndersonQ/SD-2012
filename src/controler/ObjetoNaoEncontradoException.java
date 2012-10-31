package controler;
public class ObjetoNaoEncontradoException extends Exception {
  public ObjetoNaoEncontradoException(String objName) {
    super(objName);
  }
}