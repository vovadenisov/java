package frontend;

import main.AccountService;
import main.UserProfile;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import main.UsersReadyToGameService;
import main.RoomService;
import org.junit.Before;

/**
 * Created by alla on 03.11.15.
 */
public class FindGameServletTest {
    private UserProfile testUser;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RoomService roomService = mock(RoomService.class);
    private final UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private FindGameServlet findGameServlet;

    @Before
    public void initialization() throws Exception {
        findGameServlet = new FindGameServlet(accountService, usersReadyToGameService, roomService);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
    }

    /*
     @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        //если юзера нет
        if(!accountService.checkSeassions(request.getSession().getId())) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", SignInServlet.SIGNIN_PAGE_URL);
        }
        else {
            if (request.getParameter("is_game") == null) {
                Map<String, Object> pageVariables = new HashMap<>();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(PageGenerator.getPage("find_list.html", pageVariables));
            }
            if (request.getParameter("is_game") != null){
                UserProfile current_user = accountService.getCurrentUser(request.getSession().getId());
                Boolean find = false;
                JSONObject json = new JSONObject();
                for (int counter = 0; counter < 10; counter++){
                    if (roomService.userInRoom(current_user)){
                        response.setStatus(HttpServletResponse.SC_OK);
                        json.put("game_status",1);
                        response.getWriter().println(json);
                        find = true;
                        break;
                    }
                    Time.sleep(1000);
                }
                if (!find){
                    response.setStatus(HttpServletResponse.SC_OK);
                    json.put("game_status",0);
                    response.getWriter().println(json);
                }
            }
        }
    }
     */
    @Test
    public void testDoGetAnonim() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        findGameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FOUND);
        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPost() throws Exception {


    }
}