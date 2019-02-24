import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {NgForm} from '@angular/forms';
@Component({
  selector: 'app-translate',
  templateUrl: './translate.component.html',
  styleUrls: ['./translate.component.css']
})
export class TranslateComponent implements OnInit {
  @ViewChild('place') places: ElementRef;
  placeValue: any;
  venueList = [];
  result: any;
  // Hard coded list of languages, along with their short code
  categories = [
    {'long': 'English', 'short': 'en'},
    {'long': 'French', 'short': 'fr'},
    {'long': 'German', 'short': 'de'},
    {'long': 'Chinese', 'short': 'zh'},
    {'long': 'Japanese', 'short': 'ja'},
    {'long': 'Spanish', 'short': 'es'},
    {'long': 'Vietnamese', 'short': 'vi'},
    {'long': 'Russian', 'short': 'ru'}
    ];
  submitted = false;
  lang1val: any;
  lang2val: any;
  lang1full: string;
  lang2full: string;
  orgText: string;
  resultText: string

  constructor(private _http: HttpClient) {
  }


  /**
   * This function executes when user clicks the submit button on the front end
   * @param f: the form passed from the .html file that contains the to-be-translated text and user's language preferences.
   */
  onSubmit(f: NgForm) {
    this.submitted = true;
    console.log(f.value);
    console.log(f.value.lang1);
    console.log(f.value.text);
    //Bind these values to local variables to reduce accessing
    this.lang1val = f.value.lang1;
    this.lang2val = f.value.lang2;
    this.orgText = f.value.text;
    this.lang1full = this.getLongLang(this.lang1val);
    this.lang2full = this.getLongLang(this.lang2val);
    console.log(this.lang1full);
    console.log(this.lang2full);
    //Api call
    if (this.lang1val != null && this.lang2val != '') {
      this._http.get('https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20151023T145251Z.bf1ca7097253ff7e.c0b0a88bea31ba51f72504cc0cc42cf891ed90d2' +
        '&text=' + this.orgText +
        '&lang=' + this.lang1val + '-' + this.lang2val +
        '&[format=plain]' +
        '&[options=1]&[callback=set]')
        .subscribe((data: any) => {
            this.result = {
              'lang1': this.lang1full,
              'lang2': this.lang2full,
              'resulttext': data.text[0]
            };
            console.log(this.result.resulttext);
            //Pass this back to html side
            this.resultText = this.result.resulttext;

        });
    }
  }

  /**
   * This function will return the matching long name
   * @param short
   * @returns {string}
   */
  getLongLang(short) {
    for (let i = 0; i < this.categories.length; i++){
      if (short == this.categories[i].short){
        return this.categories[i].long;
      }
    }
  }
}
