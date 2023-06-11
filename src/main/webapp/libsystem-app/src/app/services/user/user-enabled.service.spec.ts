import { TestBed } from '@angular/core/testing';

import { UserEnabledService } from './user-enabled.service';

describe('UserEnabledService', () => {
  let service: UserEnabledService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserEnabledService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
