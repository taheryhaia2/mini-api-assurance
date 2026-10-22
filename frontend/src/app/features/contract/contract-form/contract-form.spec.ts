import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractForm } from './contract-form';

describe('ContractForm', () => {
  let component: ContractForm;
  let fixture: ComponentFixture<ContractForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ContractForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
