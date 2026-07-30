import { TestBed } from '@angular/core/testing';

import { Contract } from './contract';

describe('Contract', () => {
  let service: Contract;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Contract);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
