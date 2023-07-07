import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtendBookComponent } from './extend-book.component';

describe('ExtendBookComponent', () => {
  let component: ExtendBookComponent;
  let fixture: ComponentFixture<ExtendBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExtendBookComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExtendBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
