/* Generated By:JavaCC: Do not edit this line. CTBParser100.java */
import java.util.Stack;

public class CTBParser100 extends CTBParser implements CTBParser100Constants {

///////////////////////////////////////////////
//	StringToken
///////////////////////////////////////////////
  final public String StringToken() throws ParseException {
        Token   t;
    t = jj_consume_token(STRING);
                        {if (true) return t.image.substring(1, t.image.length()-1);}
    throw new Error("Missing return statement in function");
  }

///////////////////////////////////////////////
//	IntegerToken
///////////////////////////////////////////////
  final public int IntegerToken() throws ParseException {
        Token t;
    t = jj_consume_token(NUMBER);
                        {if (true) return Integer.parseInt(t.image);}
    throw new Error("Missing return statement in function");
  }

///////////////////////////////////////////////
//	Event
///////////////////////////////////////////////
  final public void EventElement() throws ParseException {
        String  value;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case Name:
      jj_consume_token(Name);
      value = StringToken();
                        getCurrentEvent().setName(value);
      break;
    case Option:
      jj_consume_token(Option);
      value = StringToken();
                        getCurrentEvent().setOptionString(value);
      break;
    case Type:
      jj_consume_token(Type);
      value = StringToken();
                        getCurrentEvent().setAttribute(value);
      break;
    case Diagram:
      Diagram();
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void EventID() throws ParseException {
    jj_consume_token(Event);
                        Event event = new Event();
                        event.setWorld(getWorld());
                        addEvent(event);
                        pushObject(event);
  }

  final public void Event() throws ParseException {
    EventID();
    jj_consume_token(18);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case Diagram:
      case Name:
      case Option:
      case Type:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      EventElement();
    }
    jj_consume_token(19);
                        Event   event = getCurrentEvent();

                        String  name                                    = event.getName();
                        String  optionString            = event.getOptionString();
                        int             attribute                       = event.getAttribute();

                        EventType eventType = null;

                        switch (attribute) {
                        case EventType.ATTRIBUTE_SYSTEM:
                                {
                                        eventType = getWorld().getSystemEventType(name);
                                }
                                break;
                        case EventType.ATTRIBUTE_USER:
                                {
                                        eventType = getWorld().getUserEventType(name);
                                        event.setOptionString(optionString);
                                }
                                break;
                        default: // case EventType.ATTRIBUTE_NONE:
                                {
                                        if (optionString != null) {
                                                eventType = getWorld().getUserEventType(name);
                                                event.setOptionString(optionString);
                                        }
                                        else
                                                eventType = getWorld().getSystemEventType(name);
                                }
                                break;
                        }

                        event.setEventType(eventType);

                        popObject();
  }

///////////////////////////////////////////////
//	Diagram
///////////////////////////////////////////////
  final public void DiagramElement() throws ParseException {
        String  value;
        int             x, y;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case Name:
      jj_consume_token(Name);
      value = StringToken();
                        getCurrentDiagram().setName(value);
      break;
    case Pos:
      jj_consume_token(Pos);
      x = IntegerToken();
      y = IntegerToken();
                        getCurrentDiagram().setPosition(x, y);
      break;
    case Size:
      jj_consume_token(Size);
      x = IntegerToken();
      y = IntegerToken();
                        getCurrentDiagram().setWidth(x);
                        getCurrentDiagram().setHeight(y);
      break;
    case Module:
      Module();
      break;
    case Dataflow:
      Dataflow();
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void DiagramID() throws ParseException {
    jj_consume_token(Diagram);
                        Event event = getCurrentEvent();
                        Diagram dgm = new Diagram();
                        event.addDiagram(dgm);
                        pushObject(dgm);
  }

  final public void Diagram() throws ParseException {
    DiagramID();
    jj_consume_token(18);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case Module:
      case Name:
      case Pos:
      case Size:
      case Dataflow:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
      DiagramElement();
    }
    jj_consume_token(19);
                        popObject();
  }

///////////////////////////////////////////////
//	Module
///////////////////////////////////////////////
  final public void ModuleElement() throws ParseException {
        String  value;
        int             x, y;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case Class:
      jj_consume_token(Class);
      value = StringToken();
                        getCurrentModuleInfo().setClassName(value);
      break;
    case Type:
      jj_consume_token(Type);
      value = StringToken();
                        getCurrentModuleInfo().setTypeName(value);
      break;
    case Name:
      jj_consume_token(Name);
      value = StringToken();
                        getCurrentModuleInfo().setName(value);
      break;
    case Value:
      jj_consume_token(Value);
      value = StringToken();
                        getCurrentModuleInfo().setValue(value);
      break;
    case Pos:
      jj_consume_token(Pos);
      x = IntegerToken();
      y = IntegerToken();
                        getCurrentModuleInfo().setPosition(x, y);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void ModuleID() throws ParseException {
    jj_consume_token(Module);
                        ModuleInfo moduleInfo = new ModuleInfo();
                        pushObject(moduleInfo);
  }

  final public void Module() throws ParseException {
    ModuleID();
    jj_consume_token(18);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case Name:
      case Class:
      case Type:
      case Pos:
      case Value:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
      ModuleElement();
    }
    jj_consume_token(19);
                        ModuleInfo moduleInfo = getCurrentModuleInfo();
                        ModuleType modType = getWorld().getModuleType(moduleInfo.getClassName(), moduleInfo.getTypeName());

                        popObject();

                        if (modType != null) {
                                Diagram dgm = getCurrentDiagram();
                                Module module = dgm.addModule(modType);
                                if (module != null) {
                                        module.setName(moduleInfo.getName());
                                        module.setValue(moduleInfo.getValue());
                                        module.setPosition(moduleInfo.getXPosition(), moduleInfo.getYPosition());
                                }
                        }
                        else
                                Debug.warning("Coundn't find the module type (" + moduleInfo.getClassName() + ", " + moduleInfo.getTypeName() + ")");
  }

///////////////////////////////////////////////
//	Dataflow
///////////////////////////////////////////////
  final public String ModuleNameToken() throws ParseException {
        Token   t;
    t = jj_consume_token(ID);
                        {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String NodeNameToken() throws ParseException {
        Token   t;
    t = jj_consume_token(ID);
                        {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public int NodeNumberToken() throws ParseException {
        int     number;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
      number = IntegerToken();
                        {if (true) return number;}
      break;
    case 20:
      jj_consume_token(20);
                        {if (true) return -1;}
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void Dataflow() throws ParseException {
        String  OutModuleName;
        String  OutNodeName;
        String  InModuleName;
        String  InNodeName;
        int             InNodeNumber;
        int             OutNodeNumber;
    if (jj_2_1(4)) {
      jj_consume_token(Dataflow);
      OutModuleName = ModuleNameToken();
      jj_consume_token(21);
      OutNodeNumber = NodeNumberToken();
      jj_consume_token(To);
      InModuleName = ModuleNameToken();
      jj_consume_token(21);
      InNodeNumber = NodeNumberToken();
                        getCurrentDiagram().addDataflow(OutModuleName, OutNodeNumber, InModuleName, InNodeNumber);
    } else if (jj_2_2(4)) {
      jj_consume_token(Dataflow);
      OutModuleName = ModuleNameToken();
      jj_consume_token(21);
      OutNodeName = NodeNameToken();
      jj_consume_token(To);
      InModuleName = ModuleNameToken();
      jj_consume_token(21);
      InNodeName = NodeNameToken();
                        getCurrentDiagram().addDataflow(OutModuleName, OutNodeName, InModuleName, InNodeName);
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Input() throws ParseException {
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case Event:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_4;
      }
      Event();
    }
    jj_consume_token(0);
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_1();
    jj_save(0, xla);
    return retval;
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_2();
    jj_save(1, xla);
    return retval;
  }

  final private boolean jj_3R_5() {
    if (jj_scan_token(ID)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_10() {
    if (jj_scan_token(NUMBER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_scan_token(Dataflow)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_5()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(21)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_7()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_scan_token(Dataflow)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_5()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(21)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_3R_6()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_9() {
    if (jj_scan_token(20)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_8() {
    if (jj_3R_10()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_6() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_8()) {
    jj_scanpos = xsp;
    if (jj_3R_9()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_7() {
    if (jj_scan_token(ID)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  public CTBParser100TokenManager token_source;
  ASCII_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[8];
  final private int[] jj_la1_0 = {0x1680,0x1680,0x16300,0x16300,0xba00,0xba00,0x500000,0x40,};
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public CTBParser100(java.io.InputStream stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new CTBParser100TokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public CTBParser100(java.io.Reader stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new CTBParser100TokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public CTBParser100(CTBParser100TokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(CTBParser100TokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    return (jj_scanpos.kind != kind);
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration enum = jj_expentries.elements(); enum.hasMoreElements();) {
        int[] oldentry = (int[])(enum.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[25];
    for (int i = 0; i < 25; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 25; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
