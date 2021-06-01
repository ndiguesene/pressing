import { IProduit } from 'app/entities/produit/produit.model';
import { StatusCategorie } from 'app/entities/enumerations/status-categorie.model';

export interface ICategorie {
  id?: number;
  designation?: string;
  description?: string | null;
  status?: StatusCategorie | null;
  produits?: IProduit[] | null;
}

export class Categorie implements ICategorie {
  constructor(
    public id?: number,
    public designation?: string,
    public description?: string | null,
    public status?: StatusCategorie | null,
    public produits?: IProduit[] | null
  ) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
