import { TcgAutoPage } from './app.po';

describe('tcg-auto App', () => {
  let page: TcgAutoPage;

  beforeEach(() => {
    page = new TcgAutoPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
