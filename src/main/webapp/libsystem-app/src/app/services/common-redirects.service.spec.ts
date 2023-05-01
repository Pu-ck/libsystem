import { TestBed } from '@angular/core/testing';

import { CommonRedirectsService } from './common-redirects.service';

describe('CommonRedirectsService', () => {
  let service: CommonRedirectsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommonRedirectsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
