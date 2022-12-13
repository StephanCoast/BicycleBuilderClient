package pf.bb.task;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import pf.bb.Main;
import pf.bb.model.Article;

public class GetArticleImageTask extends Task<Image> {

	Article article;

	public GetArticleImageTask(Article article) {
		this.article = article;
	}

	@Override
	protected Image call() throws Exception {
		String url = Main.API_HOST  + "/articles/" + article.id;

		// TODO Save images on server and parse Binary Stream
//		HttpResponse<InputStream> resImage = Unirest.get(url).header("Accept", "application/octet-stream").asBinary();
//		InputStream is = resImage.getBody();
//		product.image = new Image(is);
//		return product.image;
		return null;
	}
}
