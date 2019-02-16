import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'your To-do List';
  taskList = [];

  /**
   * addTask: push the current task to array (array will be shown on screen later)
   * @param value
   */
  addTask(value) {
    this.taskList.push(value);
    //console.log(this.task);
  }

  /**
   * Delete current task out of array
   * @param task: current task to delete
   */
  deleteTask(task) {
    for (let i = 0 ; i <= this.taskList.length ; i++) {
      if (task == this.taskList[i]) {
        this.taskList.splice(i, 1);
      }
    }
  }
}
