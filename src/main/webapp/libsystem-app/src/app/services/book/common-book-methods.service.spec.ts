import { TestBed } from '@angular/core/testing';

import { CommonBookMethodsService } from './common-book-methods.service';

describe('CommonBookMethodsService', () => {
  let service: CommonBookMethodsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommonBookMethodsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
