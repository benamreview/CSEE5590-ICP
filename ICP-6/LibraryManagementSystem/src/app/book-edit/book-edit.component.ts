import {Component, Inject, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {ApiService} from '../api.service';
import {FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
export interface DialogData {
  animal: string;
  name: string;
}
@Component({
  selector: 'app-book-edit',
  templateUrl: './book-edit.component.html',
  styleUrls: ['./book-edit.component.css']
})
export class BookEditComponent implements OnInit {
  animal: string;
  name: string;
  book = {};
  bookForm: FormGroup;
  isbn = '';
  title = '';
  description = '';
  author = '';
  publisher = '';
  published_year = '';
  constructor(public dialog: MatDialog, private route: ActivatedRoute,
              private api: ApiService, private router: Router,
              private formBuilder: FormBuilder) {
    console.log(this.route.snapshot.params['id']);
  }

  ngOnInit() {
    this.getBookDetails(this.route.snapshot.params['id']);
    this.bookForm = this.formBuilder.group({
      'isbn': [' ', Validators.required],
      'title': [' ', Validators.required],
      'description': [' ', Validators.required],
      'author': [' ', Validators.required],
      'publisher': [' ', Validators.required],
      'published_year': [' ', Validators.required]
    });
  }

  /**
   * This function is called to display current information of book.
   * @param id
   */
  getBookDetails(id) {
    this.api.getBook(id)
      .subscribe(data => {
        console.log(data);
        this.book = data;
      });
  }

  /**
   * This function fires when form is submitted with new data to be updated. ID is extracted using ActivatedRoute
   * @param form
   */
  onFormSubmit(form: NgForm) {
    this.api.updateBook(this.route.snapshot.params['id'], form)
      .subscribe(res => {
        const id = res['_id'];
        this.router.navigate(['/book-details', id]);
      }, (err) => {
        console.log(err);
      });
  }

  /**
   * This function is used to open a new confirmation dialog box to ask whether user wants to go back to previous page
   * and discard edit changes
   */
  openDialog(): void {
    const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
      width: '350px',
      data: {name: this.name, animal: this.animal}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    });
  }

}
@Component({
  selector: 'dialog-overview-example-dialog',
  templateUrl: 'dialog-overview-example-dialog.html',
})
export class DialogOverviewExampleDialog {

  constructor(
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  /**
   * This function fires when user clicks out of bound or the No thanks button
   */
  onNoClick(): void {
    this.dialogRef.close();
  }

}
