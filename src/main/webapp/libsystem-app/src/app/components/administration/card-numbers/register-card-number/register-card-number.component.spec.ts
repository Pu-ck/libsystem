import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterCardNumberComponent } from './register-card-number.component';

describe('RegisterCardNumberComponent', () => {
  let component: RegisterCardNumberComponent;
  let fixture: ComponentFixture<RegisterCardNumberComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterCardNumberComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterCardNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
