import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEnabledStatusComponent } from './user-enabled-status.component';

describe('UserEnabledStatusComponent', () => {
  let component: UserEnabledStatusComponent;
  let fixture: ComponentFixture<UserEnabledStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserEnabledStatusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserEnabledStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
