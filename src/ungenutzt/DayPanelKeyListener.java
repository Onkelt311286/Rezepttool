package ungenutzt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DayPanelKeyListener implements KeyListener {

  private boolean isSTRGPressed;

  public DayPanelKeyListener() {
    setSTRGPressed(false);
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == 17) {
      isSTRGPressed = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    isSTRGPressed = false;
  }

  public boolean isSTRGPressed() {
    return isSTRGPressed;
  }

  public void setSTRGPressed(boolean isSTRGPressed) {
    this.isSTRGPressed = isSTRGPressed;
  }
}
