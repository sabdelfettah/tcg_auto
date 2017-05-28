import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { TcgAutoComponent } from './app.component';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MdTabsModule, MdMenuModule, MdButtonModule, MdCheckboxModule} from '@angular/material';

import 'hammerjs';

@NgModule({
  declarations: [
    TcgAutoComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,
    MdTabsModule,
    MdMenuModule,
    MdButtonModule,
    MdCheckboxModule
  ],
  providers: [],
  bootstrap: [TcgAutoComponent]
})
export class TcgAutoAppModule { }
