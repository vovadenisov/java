package frontend;

/**
 * Created by alla on 02.11.15.
 */
//public class GameServletTest {
//    private final HttpServletRequest request = mock(HttpServletRequest.class);
//    private final HttpServletResponse response = mock(HttpServletResponse.class);
//    private final AccountService accountService = mock(AccountService.class);
//    private final HttpSession session = mock(HttpSession.class);
//    private final StringWriter stringWriter = new StringWriter();
//    final PrintWriter writer = new PrintWriter(stringWriter);
//    private GameServlet gameServlet;
//
//    @Before
//    public void initialization() throws Exception {
//        when(response.getWriter()).thenReturn(writer);
//        when(request.getSession()).thenReturn(session);
//        gameServlet = new GameServlet(accountService);
//    }
//
//    @Test
//    public void testDoGetNoRoom() throws Exception {
//        when(request.getParameter("room")).thenReturn(null);
//        gameServlet.doGet(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(request, never()).getParameter("start");
//    }
//    @Test
//    public void testDoGetRoomExistNoStart() throws Exception {
//        when(request.getParameter("room")).thenReturn("1");
//        when(request.getParameter("start")).thenReturn(null);
//        gameServlet.doGet(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(accountService, never()).getCurrentUser(request.getSession().getId());
//
//    }
//    @Test
//    public void testDoGetRoomExistStartExist() throws Exception {
//        when(request.getParameter("room")).thenReturn("1");
//        when(request.getParameter("start")).thenReturn("start");
//        gameServlet.doGet(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(accountService).getCurrentUser(request.getSession().getId());
//
//    }
//}