import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractList } from './contract-list';

describe('ContractList', () => {
  let component: ContractList;
  let fixture: ComponentFixture<ContractList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractList],
    }).compileComponents();

    fixture = TestBed.createComponent(ContractList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
