import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommandeProduit } from '../commande-produit.model';

@Component({
  selector: 'jhi-commande-produit-detail',
  templateUrl: './commande-produit-detail.component.html',
})
export class CommandeProduitDetailComponent implements OnInit {
  commandeProduit: ICommandeProduit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeProduit }) => {
      this.commandeProduit = commandeProduit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
