import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';

import {ApiService} from '../api.service';

@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  styleUrls: ['./customer-edit.component.css']
})
export class CustomerEditComponent implements OnInit {

  customerForm: FormGroup;
  id = '';
  name: object = {
    first: <string>'',
    last: <string>''
  };
  gender = '';
  birthday = '';
  lastContact = '';
  customerLifetimeValue = '';
  //customer = {};
  submitted = false;
  constructor(private router: Router, private route: ActivatedRoute, private api: ApiService, private formBuilder: FormBuilder) {
  }
  // getter for form fields
  get f() {
    return this.customerForm.controls;
  }
  ngOnInit() {
    this.getCustomer(this.route.snapshot.params['id']);
    this.customerForm = this.formBuilder.group({
      firstName: [' ', Validators.required],
      lastName: [' ', Validators.required],
      gender: [' ', Validators.required],
      birthday: [' ', Validators.required],
      lastContact: [' ', Validators.required],
      customerLifetimeValue: [' ', Validators.required],
    });
  }

  getCustomer(id) {
    /*** Get the Customer Details*/
    this.api.getCustomer(id)
      .subscribe(data => {
        // data.birthday = new Date(data.birthday);
        console.log(data);
        this.customerForm.patchValue({
          firstName : data.name.first,
          lastName: data.name.last,
          gender: data.gender,
          birthday: new Date(data.birthday),
          lastContact: data.lastContact,
          customerLifetimeValue: data.customerLifetimeValue
        })
        //this.customer = {...data};
      });
  }

  onFormSubmit(form: NgForm) {
    /*** On form submit update the customer details*/
    console.log(form);
    this.submitted = true;
    if (this.customerForm.invalid) {
      return;
    }
    let customer: object = {};
    customer['customerID'] = 15;
    customer['name'] = {
      first: this.customerForm.value.firstName,
      last: this.customerForm.value.lastName
    };
    customer['gender'] = this.customerForm.value.gender;
    customer['birthday'] = this.customerForm.value.birthday;
    customer['lastContact'] = this.customerForm.value.lastContact;
    customer['customerLifetimeValue'] = this.customerForm.value.customerLifetimeValue;
    console.log(customer);
    this.api.updateCustomer(this.route.snapshot.params['id'], customer)
      .subscribe(res => {
        let id = res['_id'];
        this.router.navigate(['/customer-details', id]);
      }, (err) => {
        console.log(err);
      });
  }
  onReset() {
    this.submitted = false;
    this.customerForm.reset();
  }
}
