import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEnabledStatusUpdatedComponent } from './user-enabled-status-updated.component';

describe('UserEnabledStatusUpdatedComponent', () => {
  let component: UserEnabledStatusUpdatedComponent;
  let fixture: ComponentFixture<UserEnabledStatusUpdatedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserEnabledStatusUpdatedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserEnabledStatusUpdatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
