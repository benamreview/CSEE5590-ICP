import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'calculatorApp';
  currentString = '';
  currentEquation = '';
  elementsArray = [];

  /**
   * Clears the entire string, equation, and screen
   */
  clear() {
    this.currentEquation = '';
    this.currentString = '';
    this.elementsArray = [];
  }

  /**
   * Attach a symbol along with any entered number so far to the screen
   * @param symbol: any kind of symbol such as + - * /
   */
  equationAdd(symbol) {
    this.elementsArray.push(this.currentString);
    this.elementsArray.push(symbol);
    this.currentEquation += this.currentString + symbol;
  }

  /**
   * Clear current number on screen
   */
  clearNumber() {
    this.currentString = '';
  }

  /**
   * multiply, divide, substract, modulo, and add are five
   * basic calculation of a calculator
   * @param a: first number
   * @param b: second number
   */
  multiply(a, b) {
    const c = Number(this.elementsArray[a]) * Number(this.elementsArray[b]);
    this.elementsArray[a] = c.toString();
    this.elementsArray.splice(a + 1, 2);
  }

  divide(a, b) {
    const c = Number(this.elementsArray[a]) / Number(this.elementsArray[b]);
    this.elementsArray[a] = c.toString();
    this.elementsArray.splice(a + 1, 2);
  }

  modulo(a, b) {
    const c = Number(this.elementsArray[a]) % Number(this.elementsArray[b]);
    this.elementsArray[a] = c.toString();
    this.elementsArray.splice(a + 1, 2);
  }

  add(a, b) {
    const c = Number(this.elementsArray[a]) + Number(this.elementsArray[b]);
    this.elementsArray[a] = c.toString();
    this.elementsArray.splice(a + 1, 2);
  }

  substract(a, b) {
    const c = Number(this.elementsArray[a]) - Number(this.elementsArray[b]);
    this.elementsArray[a] = c.toString();
    this.elementsArray.splice(a + 1, 2);
  }

  /**
   * Main function that executes the calculation in series
   */
  calculate() {
    for (var i = 0; i < this.elementsArray.length; i++) {
      if (this.elementsArray[i] == "*") {
        this.multiply(i - 1, i + 1);
        i = i - 2;
      } else if (this.elementsArray[i] == "/") {
        this.divide(i - 1, i + 1);
        i = i - 2;
      } else if (this.elementsArray[i] == "%") {
        this.modulo(i - 1, i + 1);
        i = i - 2;
      }
    }
    for (i = 0; i < this.elementsArray.length; i++) {
      if (this.elementsArray[i] == "+") {
        this.add(i - 1, i + 1);
        i = i - 2;
      } else if (this.elementsArray[i] == "-") {
        this.substract(i - 1, i + 1);
        i = i - 2;
      }
    }
  }

  /**
   * This is a wrapper function that takes in a value of a button and process its semantic
   * @param num: value of the button that is clicked on screen (either a number of symbol)
   */
  appendNum(num) {
    switch (num) {
      case '+':
      {
        this.equationAdd('+');
        this.clearNumber();
        break;
      }
      case '-':
      {
        this.equationAdd('-');
        this.clearNumber();
        break;
      }

      case '*':
      {
        this.equationAdd('*');
        this.clearNumber();
        break;
      }
      case '/':
      {
        this.equationAdd('/');
        this.clearNumber();
        break;
      }

      case '%':
      {
        this.equationAdd('%');
        this.clearNumber();
        break;
      }
      case 'Clear':
      {
        this.clear();
        break;

      }

      case '=':
      {
        this.equationAdd('=');
        this.elementsArray.pop();
        this.calculate();
        this.currentString = this.elementsArray[0];
        this.currentEquation = '';
        this.elementsArray.pop();
        break;
      }

      default:
      {
        this.currentString += num;
        break;
      }

    }

  }
}
