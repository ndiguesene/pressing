import { ICategorie } from 'app/entities/categorie/categorie.model';
import { ICommandeProduit } from 'app/entities/commande-produit/commande-produit.model';
import { Taille } from 'app/entities/enumerations/taille.model';

export interface IProduit {
  id?: number;
  designation?: string;
  prix?: number;
  codeProduit?: string | null;
  tailleProduit?: Taille;
  imageContentType?: string | null;
  image?: string | null;
  categorie?: ICategorie | null;
  commandeProduits?: ICommandeProduit[] | null;
}

export class Produit implements IProduit {
  constructor(
    public id?: number,
    public designation?: string,
    public prix?: number,
    public codeProduit?: string | null,
    public tailleProduit?: Taille,
    public imageContentType?: string | null,
    public image?: string | null,
    public categorie?: ICategorie | null,
    public commandeProduits?: ICommandeProduit[] | null
  ) {}
}

export function getProduitIdentifier(produit: IProduit): number | undefined {
  return produit.id;
}
