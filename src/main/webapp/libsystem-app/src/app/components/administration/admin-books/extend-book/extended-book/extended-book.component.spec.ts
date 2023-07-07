import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtendedBookComponent } from './extended-book.component';

describe('ExtendedBookComponent', () => {
  let component: ExtendedBookComponent;
  let fixture: ComponentFixture<ExtendedBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExtendedBookComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExtendedBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
