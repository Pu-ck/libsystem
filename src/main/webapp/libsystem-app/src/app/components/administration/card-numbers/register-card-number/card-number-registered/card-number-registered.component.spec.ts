import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardNumberRegisteredComponent } from './card-number-registered.component';

describe('CardNumberRegisteredComponent', () => {
  let component: CardNumberRegisteredComponent;
  let fixture: ComponentFixture<CardNumberRegisteredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CardNumberRegisteredComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CardNumberRegisteredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
