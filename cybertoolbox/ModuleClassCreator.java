public class ModuleClassCreator extends Object {
    static public Module createModule(ModuleType moduleType) throws NoClassDefFoundError{
        String className = moduleType.getClassName();
        String name = moduleType.getName();
        if (className.equals("System") && name.equals("Frame"))
            return new SystemFrame();
        if (className.equals("System") && name.equals("Keyboard"))
            return new SystemKeyboard();
        if (className.equals("System") && name.equals("Mouse"))
            return new SystemMouse();
        if (className.equals("System") && name.equals("Pickup"))
            return new SystemPickup();
        if (className.equals("System") && name.equals("Clock"))
            return new SystemClock();
        if (className.equals("System") && name.equals("Area"))
            return new SystemArea();
        if (className.equals("System") && name.equals("Collision"))
            return new SystemCollision();
        if (className.equals("System") && name.equals("Message"))
            return new SystemMessage();
        if (className.equals("System") && name.equals("Input"))
            return new SystemInput();
        if (className.equals("System") && name.equals("Output"))
            return new SystemOutput();
        if (className.equals("Math") && name.equals("Abs"))
            return new MathAbs();
        if (className.equals("Math") && name.equals("Increment"))
            return new MathIncrement();
        if (className.equals("Math") && name.equals("Decrement"))
            return new MathDecrement();
        if (className.equals("Math") && name.equals("Negative"))
            return new MathNegative();
        if (className.equals("Math") && name.equals("Pow"))
            return new MathPow();
        if (className.equals("Math") && name.equals("Sqrt"))
            return new MathSqrt();
        if (className.equals("Math") && name.equals("Min"))
            return new MathMin();
        if (className.equals("Math") && name.equals("Max"))
            return new MathMax();
        if (className.equals("Math") && name.equals("Log"))
            return new MathLog();
        if (className.equals("Math") && name.equals("Exp"))
            return new MathExp();
        if (className.equals("Math") && name.equals("Sin"))
            return new MathSin();
        if (className.equals("Math") && name.equals("Cos"))
            return new MathCos();
        if (className.equals("Math") && name.equals("Tan"))
            return new MathTan();
        if (className.equals("Math") && name.equals("ASin"))
            return new MathASin();
        if (className.equals("Math") && name.equals("ACos"))
            return new MathACos();
        if (className.equals("Math") && name.equals("ATan"))
            return new MathATan();
        if (className.equals("Math") && name.equals("Degree2Radian"))
            return new MathDegree2Radian();
        if (className.equals("Math") && name.equals("Radian2Degree"))
            return new MathRadian2Degree();
        if (className.equals("Boolean") && name.equals("Equal"))
            return new BooleanEqual();
        if (className.equals("Boolean") && name.equals("NotEqual"))
            return new BooleanNotEqual();
        if (className.equals("Boolean") && name.equals("Greater"))
            return new BooleanGreater();
        if (className.equals("Boolean") && name.equals("Less"))
            return new BooleanLess();
        if (className.equals("Boolean") && name.equals("EqualGreater"))
            return new BooleanEqualGreater();
        if (className.equals("Boolean") && name.equals("EqualLess"))
            return new BooleanEqualLess();
        if (className.equals("Boolean") && name.equals("Not"))
            return new BooleanNot();
        if (className.equals("Boolean") && name.equals("And"))
            return new BooleanAnd();
        if (className.equals("Boolean") && name.equals("Or"))
            return new BooleanOr();
        if (className.equals("Filter") && name.equals("Offset"))
            return new FilterOffset();
        if (className.equals("Filter") && name.equals("Mul"))
            return new FilterMul();
        if (className.equals("Filter") && name.equals("Div"))
            return new FilterDiv();
        if (className.equals("Filter") && name.equals("Ceil"))
            return new FilterCeil();
        if (className.equals("Filter") && name.equals("Floor"))
            return new FilterFloor();
        if (className.equals("Filter") && name.equals("High"))
            return new FilterHigh();
        if (className.equals("Filter") && name.equals("Low"))
            return new FilterLow();
        if (className.equals("Filter") && name.equals("Range"))
            return new FilterRange();
        if (className.equals("String") && name.equals("Value"))
            return new StringValue();
        if (className.equals("String") && name.equals("Position"))
            return new StringPosition();
        if (className.equals("String") && name.equals("Direction"))
            return new StringDirection();
        if (className.equals("String") && name.equals("Rotation"))
            return new StringRotation();
        if (className.equals("String") && name.equals("Bool"))
            return new StringBool();
        if (className.equals("String") && name.equals("Color"))
            return new StringColor();
        if (className.equals("String") && name.equals("PI"))
            return new StringPI();
        if (className.equals("String") && name.equals("E"))
            return new StringE();
        if (className.equals("String") && name.equals("Gravity"))
            return new StringGravity();
        if (className.equals("String") && name.equals("Divide2Values"))
            return new StringDivide2Values();
        if (className.equals("String") && name.equals("Divide3Values"))
            return new StringDivide3Values();
        if (className.equals("String") && name.equals("Divide4Values"))
            return new StringDivide4Values();
        if (className.equals("String") && name.equals("Merge2Values"))
            return new StringMerge2Values();
        if (className.equals("String") && name.equals("Merge3Values"))
            return new StringMerge3Values();
        if (className.equals("String") && name.equals("Merge4Values"))
            return new StringMerge4Values();
        if (className.equals("String") && name.equals("Selector"))
            return new StringSelector();
        if (className.equals("Numeric") && name.equals("Add"))
            return new NumericAdd();
        if (className.equals("Numeric") && name.equals("Sub"))
            return new NumericSub();
        if (className.equals("Numeric") && name.equals("Mul"))
            return new NumericMul();
        if (className.equals("Numeric") && name.equals("Div"))
            return new NumericDiv();
        if (className.equals("Numeric") && name.equals("Mod"))
            return new NumericMod();
        if (className.equals("Numeric") && name.equals("And"))
            return new NumericAnd();
        if (className.equals("Numeric") && name.equals("Or"))
            return new NumericOr();
        if (className.equals("Numeric") && name.equals("Xor"))
            return new NumericXor();
        if (className.equals("Geometry") && name.equals("Normalize"))
            return new GeometryNormalize();
        if (className.equals("Geometry") && name.equals("Inverse"))
            return new GeometryInverse();
        if (className.equals("Geometry") && name.equals("GetLength"))
            return new GeometryGetLength();
        if (className.equals("Geometry") && name.equals("GetDot"))
            return new GeometryGetDot();
        if (className.equals("Geometry") && name.equals("GetAngle"))
            return new GeometryGetAngle();
        if (className.equals("Geometry") && name.equals("GetVector"))
            return new GeometryGetVector();
        if (className.equals("Geometry") && name.equals("GetDistance"))
            return new GeometryGetDistance();
        if (className.equals("Geometry") && name.equals("GetCross"))
            return new GeometryGetCross();
        if (className.equals("Geometry") && name.equals("Rotate"))
            return new GeometryRotate();
        if (className.equals("Transform") && name.equals("GetName"))
            return new TransformGetName();
        if (className.equals("Transform") && name.equals("SetTranslation"))
            return new TransformSetTranslation();
        if (className.equals("Transform") && name.equals("SetRotation"))
            return new TransformSetRotation();
        if (className.equals("Transform") && name.equals("SetScale"))
            return new TransformSetScale();
        if (className.equals("Transform") && name.equals("SetCenter"))
            return new TransformSetCenter();
        if (className.equals("Transform") && name.equals("GetTranslation"))
            return new TransformGetTranslation();
        if (className.equals("Transform") && name.equals("GetRotation"))
            return new TransformGetRotation();
        if (className.equals("Transform") && name.equals("GetScale"))
            return new TransformGetScale();
        if (className.equals("Transform") && name.equals("GetCenter"))
            return new TransformGetCenter();
        if (className.equals("Light") && name.equals("GetName"))
            return new LightGetName();
        if (className.equals("Light") && name.equals("SetOn"))
            return new LightSetOn();
        if (className.equals("Light") && name.equals("SetColor"))
            return new LightSetColor();
        if (className.equals("Light") && name.equals("SetIntensity"))
            return new LightSetIntensity();
        if (className.equals("Light") && name.equals("SetLocation"))
            return new LightSetLocation();
        if (className.equals("Light") && name.equals("SetDirection"))
            return new LightSetDirection();
        if (className.equals("Light") && name.equals("SetRadius"))
            return new LightSetRadius();
        if (className.equals("Light") && name.equals("GetOn"))
            return new LightGetOn();
        if (className.equals("Light") && name.equals("GetColor"))
            return new LightGetColor();
        if (className.equals("Light") && name.equals("GetIntensity"))
            return new LightGetIntensity();
        if (className.equals("Light") && name.equals("GetLocation"))
            return new LightGetLocation();
        if (className.equals("Light") && name.equals("GetDirection"))
            return new LightGetDirection();
        if (className.equals("Light") && name.equals("GetRadius"))
            return new LightGetRadius();
        if (className.equals("Material") && name.equals("GetName"))
            return new MaterialGetName();
        if (className.equals("Material") && name.equals("SetAmbientIntensity"))
            return new MaterialSetAmbientIntensity();
        if (className.equals("Material") && name.equals("SetDiffuseColor"))
            return new MaterialSetDiffuseColor();
        if (className.equals("Material") && name.equals("SetEmissiveColor"))
            return new MaterialSetEmissiveColor();
        if (className.equals("Material") && name.equals("SetShininess"))
            return new MaterialSetShininess();
        if (className.equals("Material") && name.equals("SetTransparency"))
            return new MaterialSetTransparency();
        if (className.equals("Material") && name.equals("GetAmbientIntensity"))
            return new MaterialGetAmbientIntensity();
        if (className.equals("Material") && name.equals("GetDiffuseColor"))
            return new MaterialGetDiffuseColor();
        if (className.equals("Material") && name.equals("GetEmissiveColor"))
            return new MaterialGetEmissiveColor();
        if (className.equals("Material") && name.equals("GetShininess"))
            return new MaterialGetShininess();
        if (className.equals("Material") && name.equals("GetTransparency"))
            return new MaterialGetTransparency();
        if (className.equals("Viewpoint") && name.equals("GetName"))
            return new ViewpointGetName();
        if (className.equals("Viewpoint") && name.equals("SetPosition"))
            return new ViewpointSetPosition();
        if (className.equals("Viewpoint") && name.equals("SetOrientation"))
            return new ViewpointSetOrientation();
        if (className.equals("Viewpoint") && name.equals("SetFOV"))
            return new ViewpointSetFOV();
        if (className.equals("Viewpoint") && name.equals("GetPosition"))
            return new ViewpointGetPosition();
        if (className.equals("Viewpoint") && name.equals("GetOrientation"))
            return new ViewpointGetOrientation();
        if (className.equals("Viewpoint") && name.equals("GetFOV"))
            return new ViewpointGetFOV();
        if (className.equals("Interpolator") && name.equals("GetName"))
            return new InterpolatorGetName();
        if (className.equals("Interpolator") && name.equals("Play"))
            return new InterpolatorPlay();
        if (className.equals("Interpolator") && name.equals("Stop"))
            return new InterpolatorStop();
        if (className.equals("Interpolator") && name.equals("Pose"))
            return new InterpolatorPose();
        if (className.equals("Interpolator") && name.equals("IsPlaying"))
            return new InterpolatorIsPlaying();
        if (className.equals("Interpolator") && name.equals("Rewind"))
            return new InterpolatorRewind();
        if (className.equals("Interpolator") && name.equals("Next"))
            return new InterpolatorNext();
        if (className.equals("Interpolator") && name.equals("Prev"))
            return new InterpolatorPrev();
        if (className.equals("Interpolator") && name.equals("SetFraction"))
            return new InterpolatorSetFraction();
        if (className.equals("Interpolator") && name.equals("GetFraction"))
            return new InterpolatorGetFraction();
        if (className.equals("Sensor") && name.equals("Mouse"))
            return new SensorMouse();
        if (className.equals("Sensor") && name.equals("Magellan"))
            return new SensorMagellan();
        if (className.equals("Sensor") && name.equals("Fastrak"))
            return new SensorFastrak();
        if (className.equals("Sensor") && name.equals("Isotrak2"))
            return new SensorIsotrak2();
        if (className.equals("Sensor") && name.equals("IS300"))
            return new SensorIS300();
        if (className.equals("Sensor") && name.equals("Joystick"))
            return new SensorJoystick();
        if (className.equals("Sensor") && name.equals("BeeBox"))
            return new SensorBeeBox();
        if (className.equals("Misc") && name.equals("SendMessage"))
            return new MiscSendMessage();
        if (className.equals("Misc") && name.equals("InsideDiagram"))
            return new MiscInsideDiagram();
        if (className.equals("Misc") && name.equals("SetData"))
            return new MiscSetData();
        if (className.equals("Misc") && name.equals("GetData"))
            return new MiscGetData();
        if (className.equals("Misc") && name.equals("SetArray"))
            return new MiscSetArray();
        if (className.equals("Misc") && name.equals("GetArray"))
            return new MiscGetArray();
        if (className.equals("Misc") && name.equals("SetSwitch"))
            return new MiscSetSwitch();
        if (className.equals("Misc") && name.equals("SetSkyColor"))
            return new MiscSetSkyColor();
        if (className.equals("Misc") && name.equals("SetText"))
            return new MiscSetText();
        if (className.equals("Misc") && name.equals("GetTime"))
            return new MiscGetTime();
        if (className.equals("Misc") && name.equals("Random"))
            return new MiscRandom();
        if (className.equals("Misc") && name.equals("Beep"))
            return new MiscBeep();
        if (className.equals("Misc") && name.equals("PlaySound"))
            return new MiscPlaySound();
        if (className.equals("Misc") && name.equals("ShowDocument"))
            return new MiscShowDocument();
        if (className.equals("Misc") && name.equals("ShowStatus"))
            return new MiscShowStatus();
        if (className.equals("Misc") && name.equals("JavaConsole"))
            return new MiscJavaConsole();
        if (className.equals("Misc") && name.equals("GetScreenSize"))
            return new MiscGetScreenSize();
        if (className.equals("Filter") && name.equals("Scale"))
            return new FilterScale();
        if (className.equals("Interpolator") && name.equals("Replay"))
            return new InterpolatorReplay();
        Debug.warning("\tCouldn't create module " + moduleType);
        return null;
    }
}
