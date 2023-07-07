import { TestBed } from '@angular/core/testing';

import { CommonItemMethodsService } from './common-item-methods.service';

describe('CommonItemMethodsServiceService', () => {
  let service: CommonItemMethodsService

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommonItemMethodsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
