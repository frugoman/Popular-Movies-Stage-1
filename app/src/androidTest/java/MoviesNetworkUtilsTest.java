import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieReview;
import com.frusoft.movier.model.MovieVideo;
import com.frusoft.movier.util.MoviesNetworkUtils;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created by nfrugoni on 20/11/17.
 */
public class MoviesNetworkUtilsTest {

    @Test
    public void getMovieVideosTest(){
        Movie movie = new Movie();
        movie.setId(346364);
        try {
            List<MovieVideo> videos = MoviesNetworkUtils.getMovieVideosFormMovie(movie);
            Assert.assertTrue(videos.size() == 2);
            movie.setMovieVideos(videos);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getMovieReviewsTest(){
        Movie movie = new Movie();
        movie.setId(346364);
        try {
            List<MovieReview> reviews = MoviesNetworkUtils.getMovieReviewFormMovie(movie);
            Assert.assertTrue(reviews.size() == 5);
            movie.setMovieReviews(reviews);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fail();
        }
    }
}
