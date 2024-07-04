import sys
import uiui
from PyQt5.QtWidgets import QApplication, QMainWindow
from display import Display


if __name__ == '__main__':
    app = QApplication(sys.argv)
    mainWnd = QMainWindow()
    ui = uiui.Ui_MainWindow()
    ui.setupUi(mainWnd)
    display1 = Display(ui, mainWnd)
    mainWnd.show()
    sys.exit(app.exec_())
