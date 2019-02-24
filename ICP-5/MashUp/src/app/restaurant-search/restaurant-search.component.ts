import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-restaurant-search',
  templateUrl: './restaurant-search.component.html',
  styleUrls: ['./restaurant-search.component.css']
})
export class RestaurantSearchComponent implements OnInit {
  @ViewChild('place') places: ElementRef;
  placeValue: any;
  venueList = [];

  currentLat: any;
  currentLong: any;
  geolocationPosition: any;

  constructor(private _http: HttpClient) {
  }

  ngOnInit() {

    window.navigator.geolocation.getCurrentPosition(
      position => {
        this.geolocationPosition = position;
        this.currentLat = position.coords.latitude;
        this.currentLong = position.coords.longitude;
        this._http.get('https://api.foursquare.com/v2/venues/search' +
          '?client_id=S1T2Y5NEXYMSXAVLSV24OONNXG14Z02EVBW3HKPO5DTVTNL3' +
          '&client_secret=BECAJGTPPQH1JHENLMG2IZO5XCSNLTWWTGXDYCOBXYOXJTI1' +
          '&v=20160215&limit=10' +
          '&ll=' + this.currentLat + ',' + this.currentLong +
          '&categoryId=4d4b7105d754a06374d81259')
          .subscribe((data: any) => {
            alert('Current displayed results are based upon your current location. \nPlease feel free to enter other cities, states, zipcodes.');
            for (let i = 0; i < data.response.venues.length; i++) {
              this.venueList[i] = {
                'name': data.response.venues[i].name,
                'id': data.response.venues[i].id,
                'location': data.response.venues[i].location
              };
              console.log(this.venueList[i]);

            }

          });
      });
  }

  /**
   * This is the main function that takes in user's input and displays 10 nearby restaurants based on the entered location.
   */
  getVenues() {
    // Client secret = BECAJGTPPQH1JHENLMG2IZO5XCSNLTWWTGXDYCOBXYOXJTI1
    // Client ID = S1T2Y5NEXYMSXAVLSV24OONNXG14Z02EVBW3HKPO5DTVTNL3
    // this.recipeValue = this.recipes.nativeElement.value;
    this.placeValue = this.places.nativeElement.value;
    if (this.placeValue != null && this.placeValue != '') {
      this._http.get('https://api.foursquare.com/v2/venues/search' +
        '?client_id=S1T2Y5NEXYMSXAVLSV24OONNXG14Z02EVBW3HKPO5DTVTNL3' +
        '&client_secret=BECAJGTPPQH1JHENLMG2IZO5XCSNLTWWTGXDYCOBXYOXJTI1' +
        '&v=20160215&limit=10' +
        '&near=' + this.placeValue +
        '&categoryId=4d4b7105d754a06374d81259')
        .subscribe((data: any) => {
          for (let i = 0; i < data.response.venues.length; i++) {
            this.venueList[i] = {
              'name': data.response.venues[i].name,
              'id': data.response.venues[i].id,
              'location': data.response.venues[i].location
            };
            console.log(this.venueList[i]);

          }

        });
    }
  }
}
