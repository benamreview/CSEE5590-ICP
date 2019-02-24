import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {SearchRecipeComponent} from "./search-recipe/search-recipe.component";
import {RestaurantSearchComponent} from "./restaurant-search/restaurant-search.component"
import {TranslateComponent} from "./translate/translate.component";

const appRoutes: Routes = [
  { path: '', redirectTo: '/restaurant-search', pathMatch: 'full' },
  { path: 'home', component: HomeComponent},
  { path: 'search-recipe', component: SearchRecipeComponent},
  { path: 'restaurant-search', component: RestaurantSearchComponent},
  { path: 'translate', component: TranslateComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
