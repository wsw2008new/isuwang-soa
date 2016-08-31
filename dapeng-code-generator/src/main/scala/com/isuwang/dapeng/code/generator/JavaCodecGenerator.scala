package com.isuwang.dapeng.code.generator

import java.util

import com.isuwang.dapeng.core.metadata.DataType.KIND
import com.isuwang.dapeng.core.metadata._

import scala.xml.Elem

/**
 * JAVA生成器
 *
 * @author tangliu
 * @date 15/9/8
 */
class JavaCodecGenerator extends CodeGenerator {

  override def generate(services: util.List[Service], outDir: String): Unit = {

  }

  def toCodecTemplate(service:Service, namespaces:util.Set[String]): Elem = {
    //val structNameCache = new util.ArrayList[String]()

    return {
      <div>package {service.namespace.substring(0, service.namespace.lastIndexOf("."))};

        import com.isuwang.dapeng.core.*;
        import com.isuwang.org.apache.thrift.*;
        import com.isuwang.org.apache.thrift.protocol.*;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;

        import java.util.Optional;
        import java.util.concurrent.CompletableFuture;
        import java.util.concurrent.Future;

        public class {service.name}Codec <block>
        {
        toStructArrayBuffer(service.structDefinitions).map{(struct:Struct)=>{
          <div>public static class {struct.name}Serializer implements TBeanSerializer{lt}{struct.getNamespace() + "." + struct.name}{gt}<block>
            {getReadMethod(struct)}{getWriteMethod(struct)}{getValidateMethod(struct)}
            @Override
            public String toString({struct.getNamespace() + "." + struct.name} bean) <block> return bean == null ? "null" : bean.toString(); </block>
          </block>
          </div>
        }}
        }

        {
        toMethodArrayBuffer(service.methods).map{(method: Method)=> {

          <div>
            public static class {method.name}_args <block>
            {toFieldArrayBuffer(method.request.getFields).map{(field: Field)=>{
              <div>
                private {toJavaDataType(field.getDataType)} {field.getName};
                public {toJavaDataType(field.getDataType)} get{field.name.charAt(0).toUpper + field.name.substring(1)}()<block>
                return this.{field.name};
              </block>
                public void set{field.name.charAt(0).toUpper + field.name.substring(1)}({toJavaDataType(field.getDataType)} {field.name})<block>
                this.{field.name} = {field.name};
              </block>
              </div>
            }
            }
            }

            @Override
            public String toString()<block>
              StringBuilder stringBuilder = new StringBuilder("<block>");
                {toFieldArrayBuffer(method.request.getFields).map{(field : Field) =>{
                  getToStringElement(field);
                }}}
                if(stringBuilder.lastIndexOf(",") > 0)
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                stringBuilder.append("</block>");

              return stringBuilder.toString();
            </block>

          </block>


            public static class {method.name}_result <block>

            {toFieldArrayBuffer(method.response.getFields()).map{(field:Field)=>
              if(field.getDataType().getKind() == DataType.KIND.VOID) {
                <div>
                  @Override
                  public String toString()<block>
                  return "<block></block>";
                </block>
                </div>
              } else {
                <div>
                  private {toJavaDataType(method.response.getFields.get(0).getDataType)} success;
                  public {toJavaDataType(method.response.getFields.get(0).getDataType)} getSuccess()<block>
                  return success;
                </block>

                  public void setSuccess({toJavaDataType(method.response.getFields.get(0).getDataType)} success)<block>
                  this.success = success;
                </block>


                  @Override
                  public String toString()<block>
                  StringBuilder stringBuilder = new StringBuilder("<block>");
                    {toFieldArrayBuffer(method.response.getFields).map{(field : Field) =>{
                      getToStringElement(field);
                    }}}
                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    stringBuilder.append("</block>");

                  return stringBuilder.toString();
                </block>

                </div>
              }
            }}
          </block>

            public static class {method.name.charAt(0).toUpper + method.name.substring(1)}_argsSerializer implements TBeanSerializer{lt}{method.name}_args{gt}<block>
            {getReadMethod(method.getRequest)}{getWriteMethod(method.getRequest)}{getValidateMethod(method.getRequest)}

            @Override
            public String toString({method.name}_args bean) <block> return bean == null ? "null" : bean.toString(); </block>

          </block>

            public static class {method.name.charAt(0).toUpper + method.name.substring(1)}_resultSerializer implements TBeanSerializer{lt}{method.name}_result{gt}<block>
            @Override
            public void read({method.response.name} bean, TProtocol iprot) throws TException<block>

              com.isuwang.org.apache.thrift.protocol.TField schemeField;
              iprot.readStructBegin();

              while(true)<block>
                schemeField = iprot.readFieldBegin();
                if(schemeField.type == com.isuwang.org.apache.thrift.protocol.TType.STOP)<block> break;</block>

                switch(schemeField.id)<block>
                  case 0:  //SUCCESS
                  if(schemeField.type == {toThriftDateType(method.response.fields.get(0).dataType)})<block>
                    {getJavaReadAndSetElement(method.response.fields.get(0))}
                  </block>else<block>
                    com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                  </block>
                  break;
                  /*
                  case 1: //ERROR
                  bean.setSoaException(new SoaException());
                  new SoaExceptionSerializer().read(bean.getSoaException(), iprot);
                  break A;
                  */
                  default:
                  com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                </block>
                iprot.readFieldEnd();
              </block>
              iprot.readStructEnd();

              validate(bean);
            </block>
            {getWriteMethod(method.getResponse)}
            {getValidateMethod(method.getResponse)}

            @Override
            public String toString({method.name}_result bean) <block> return bean == null ? "null" : bean.toString(); </block>
          </block>

            public static class {method.name}{lt}I extends {service.getNamespace + "." + service.name}{gt} extends SoaProcessFunction{lt}I, {method.name}_args, {method.name}_result, {method.name.charAt(0).toUpper + method.name.substring(1)}_argsSerializer,  {method.name.charAt(0).toUpper + method.name.substring(1)}_resultSerializer{gt}<block>
            public {method.name}()<block>
              super("{method.name}", new {method.name.charAt(0).toUpper + method.name.substring(1)}_argsSerializer(),  new {method.name.charAt(0).toUpper + method.name.substring(1)}_resultSerializer());
            </block>
            {
            if(method.doc != null && method.doc.contains("@SoaAsyncFunction") && {toJavaDataType(method.response.getFields.get(0).getDataType)}!= DataType.KIND.VOID)
              <div>
                @Override
                public {method.name}_result getResult(I iface, {method.name}_args args) throws TException<block>
                return null;
              </block>

                @Override
                public Future{lt}{method.name}_result{gt} getResultAsync(I iface, {method.name}_args args) throws TException <block>

                CompletableFuture{lt}{method.name}_result{gt} result = new CompletableFuture{lt}{gt}();
                {toFieldArrayBuffer(method.getResponse().getFields()).map{(field:Field)=>
                  <div>
                    CompletableFuture{lt}{toJavaDataType(method.response.getFields.get(0).getDataType)}{gt} realResult = (CompletableFuture{lt}{toJavaDataType(method.response.getFields.get(0).getDataType)}{gt}) iface.{method.name}({toFieldArrayBuffer(method.getRequest.getFields).map{ (field: Field) =>{<div>args.{field.name}{if(field != method.getRequest.fields.get(method.getRequest.fields.size() - 1)) <span>,</span>}</div>}}});
                  </div>
                }}
                realResult.whenComplete((str, ex) -> <block>
                  if (str != null) <block>
                    {method.name}_result r = new {method.name}_result();
                    r.setSuccess(str);
                    result.complete(r);
                  </block> else <block>
                    result.completeExceptionally(ex);
                  </block>
                </block>);
                return result;
              </block>
              </div>
            else <div>
              @Override
              public {method.name}_result getResult(I iface, {method.name}_args args) throws TException<block>
                {method.name}_result result = new {method.name}_result();
                {toFieldArrayBuffer(method.getResponse().getFields()).map{(field:Field)=>
                  if(field.getDataType().getKind() == DataType.KIND.VOID) {
                    <div>
                      iface.{method.name}({toFieldArrayBuffer(method.getRequest.getFields).map{ (field: Field) =>{<div>args.{field.name}{if(field != method.getRequest.fields.get(method.getRequest.fields.size() - 1)) <span>,</span>}</div>}}});
                    </div>
                  } else {
                    <div>
                      result.success = iface.{method.name}({toFieldArrayBuffer(method.getRequest.getFields).map{ (field: Field) =>{<div>args.{field.name}{if(field != method.getRequest.fields.get(method.getRequest.fields.size() - 1)) <span>,</span>}</div>}}});
                    </div>
                  }
                }}
                return result;
              </block>
            </div>
            }

            @Override
            public {method.name}_args getEmptyArgsInstance()<block>
              return new {method.name}_args();
            </block>

            @Override
            protected boolean isOneway()<block>
              return false;
            </block>
          </block>
          </div>
        }
        }
        }

        public static class getServiceMetadata_args <block>

          @Override
          public String toString() <block>
            return "<block></block>";
          </block>
        </block>


        public static class getServiceMetadata_result <block>

          private String success;

          public String getSuccess() <block>
            return success;
          </block>

          public void setSuccess(String success) <block>
            this.success = success;
          </block>

          @Override
          public String toString() <block>
            StringBuilder stringBuilder = new StringBuilder("<block>");
              stringBuilder.append("\"").append("success").append("\":\"").append(this.success).append("\",");
              stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
              stringBuilder.append("</block>");

            return stringBuilder.toString();
          </block>
        </block>

        public static class GetServiceMetadata_argsSerializer implements TBeanSerializer{lt}getServiceMetadata_args{gt} <block>

          @Override
          public void read(getServiceMetadata_args bean, TProtocol iprot) throws TException <block>

            com.isuwang.org.apache.thrift.protocol.TField schemeField;
            iprot.readStructBegin();

            while (true) <block>
              schemeField = iprot.readFieldBegin();
              if (schemeField.type == com.isuwang.org.apache.thrift.protocol.TType.STOP) <block>
                break;
              </block>
              switch (schemeField.id) <block>
                default:
                com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);

              </block>
              iprot.readFieldEnd();
            </block>
            iprot.readStructEnd();

            validate(bean);
          </block>


          @Override
          public void write(getServiceMetadata_args bean, TProtocol oprot) throws TException <block>

            validate(bean);
            oprot.writeStructBegin(new com.isuwang.org.apache.thrift.protocol.TStruct("getServiceMetadata_args"));
            oprot.writeFieldStop();
            oprot.writeStructEnd();
          </block>

          public void validate(getServiceMetadata_args bean) throws TException <block></block>

          @Override
          public String toString(getServiceMetadata_args bean) <block>
            return bean == null ? "null" : bean.toString();
          </block>

        </block>

        public static class GetServiceMetadata_resultSerializer implements TBeanSerializer{lt}getServiceMetadata_result{gt} <block>
          @Override
          public void read(getServiceMetadata_result bean, TProtocol iprot) throws TException <block>

            com.isuwang.org.apache.thrift.protocol.TField schemeField;
            iprot.readStructBegin();

            while (true) <block>
              schemeField = iprot.readFieldBegin();
              if (schemeField.type == com.isuwang.org.apache.thrift.protocol.TType.STOP) <block>
                break;
              </block>

              switch (schemeField.id) <block>
                case 0:  //SUCCESS
                if (schemeField.type == com.isuwang.org.apache.thrift.protocol.TType.STRING) <block>
                  bean.setSuccess(iprot.readString());
                </block> else <block>
                  com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                </block>
                break;
                default:
                com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              </block>
              iprot.readFieldEnd();
            </block>
            iprot.readStructEnd();

            validate(bean);
          </block>

          @Override
          public void write(getServiceMetadata_result bean, TProtocol oprot) throws TException <block>

            validate(bean);
            oprot.writeStructBegin(new com.isuwang.org.apache.thrift.protocol.TStruct("getServiceMetadata_result"));

            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("success", com.isuwang.org.apache.thrift.protocol.TType.STRING, (short) 0));
            oprot.writeString(bean.getSuccess());
            oprot.writeFieldEnd();

            oprot.writeFieldStop();
            oprot.writeStructEnd();
          </block>

          public void validate(getServiceMetadata_result bean) throws TException <block>

            if (bean.getSuccess() == null)
            throw new SoaException(SoaBaseCode.NotNull, "success字段不允许为空");
          </block>

          @Override
          public String toString(getServiceMetadata_result bean) <block>
            return bean == null ? "null" : bean.toString();
          </block>
        </block>

        public static class getServiceMetadata{lt}I extends {service.namespace}.{service.name}{gt} extends SoaProcessFunction{lt}I, getServiceMetadata_args, getServiceMetadata_result, GetServiceMetadata_argsSerializer, GetServiceMetadata_resultSerializer{gt} <block>
          public getServiceMetadata() <block>
            super("getServiceMetadata", new GetServiceMetadata_argsSerializer(), new GetServiceMetadata_resultSerializer());
          </block>

          @Override
          public getServiceMetadata_result getResult(I iface, getServiceMetadata_args args) throws TException <block>
            getServiceMetadata_result result = new getServiceMetadata_result();

            try (InputStreamReader isr = new InputStreamReader({service.name}Codec.class.getClassLoader().getResourceAsStream("{service.namespace}.{service.name}.xml"));
            BufferedReader in = new BufferedReader(isr)) <block>
              int len = 0;
              StringBuilder str = new StringBuilder("");
              String line;
              while ((line = in.readLine()) != null) <block>

                if (len != 0) <block>
                  str.append("\r\n").append(line);
                </block> else <block>
                  str.append(line);
                </block>
                len++;
              </block>
              result.success = str.toString();

            </block> catch (Exception e) <block>
              e.printStackTrace();
              result.success = "";
            </block>

            return result;
          </block>

          @Override
          public getServiceMetadata_args getEmptyArgsInstance() <block>
            return new getServiceMetadata_args();
          </block>

          @Override
          protected boolean isOneway() <block>
            return false;
          </block>
        </block>

        @SuppressWarnings("unchecked")
        public static class Processor{lt}I extends {service.getNamespace + "." + service.name}{gt} extends SoaBaseProcessor<block>
          public Processor(I iface)<block>
            super(iface, getProcessMap(new java.util.HashMap{lt}{gt}()));
          </block>

          @SuppressWarnings("unchecked")
          private static {lt}I extends {service.getNamespace + "." + service.name}{gt} java.util.Map{lt}String, SoaProcessFunction{lt}I, ?, ?, ? extends TBeanSerializer{lt}?{gt}, ? extends TBeanSerializer{lt}?{gt}{gt}{gt} getProcessMap(java.util.Map{lt}String, SoaProcessFunction{lt}I, ?, ?, ? extends TBeanSerializer{lt}?{gt}, ? extends TBeanSerializer{lt}?{gt}{gt}{gt} processMap)<block>
            {
            toMethodArrayBuffer(service.getMethods).map{(method: Method)=>{
              <div>
                processMap.put("{method.name}", new {method.name}());
              </div>
            }
            }
            }
            processMap.put("getServiceMetadata", new getServiceMetadata());

            return processMap;
          </block>
        </block>

        </block>
      </div>
    }
  }

  def toStructName(struct: Struct): String = {
    if (struct.getNamespace == null) {
      return struct.getName()
    } else {
      return struct.getNamespace + "." + struct.getName();
    }
  }

  def toThriftDateType(dataType:DataType): Elem = {
    dataType.kind match {
      case KIND.VOID => <div>com.isuwang.org.apache.thrift.protocol.TType.VOID</div>
      case KIND.BOOLEAN => <div>com.isuwang.org.apache.thrift.protocol.TType.BOOL</div>
      case KIND.BYTE => <div>com.isuwang.org.apache.thrift.protocol.TType.BYTE</div>
      case KIND.SHORT => <div>com.isuwang.org.apache.thrift.protocol.TType.I16</div>
      case KIND.INTEGER => <div>com.isuwang.org.apache.thrift.protocol.TType.I32</div>
      case KIND.LONG => <div>com.isuwang.org.apache.thrift.protocol.TType.I64</div>
      case KIND.DOUBLE => <div>com.isuwang.org.apache.thrift.protocol.TType.DOUBLE</div>
      case KIND.STRING => <div>com.isuwang.org.apache.thrift.protocol.TType.STRING</div>
      case KIND.MAP => <div>com.isuwang.org.apache.thrift.protocol.TType.MAP</div>
      case KIND.LIST => <div>com.isuwang.org.apache.thrift.protocol.TType.LIST</div>
      case KIND.SET => <div>com.isuwang.org.apache.thrift.protocol.TType.SET</div>
      case KIND.ENUM => <div>com.isuwang.org.apache.thrift.protocol.TType.I32</div>
      case KIND.STRUCT => <div>com.isuwang.org.apache.thrift.protocol.TType.STRUCT</div>
      case KIND.DATE => <div>com.isuwang.org.apache.thrift.protocol.TType.I64</div>
      case KIND.BIGDECIMAL => <div>com.isuwang.org.apache.thrift.protocol.TType.STRING</div>
      case KIND.BINARY => <div>com.isuwang.org.apache.thrift.protocol.TType.STRING</div>
      case _ => <div></div>
    }
  }

  def toJavaDataType(dataType:DataType): Elem = {
    dataType.kind match {
      case KIND.VOID => <div>void</div>
      case KIND.BOOLEAN => <div>Boolean</div>
      case KIND.BYTE => <div>Byte</div>
      case KIND.SHORT => <div>Short</div>
      case KIND.INTEGER => <div>Integer</div>
      case KIND.LONG => <div>Long</div>
      case KIND.DOUBLE => <div>Double</div>
      case KIND.STRING => <div>String</div>
      case KIND.BINARY => <div>java.nio.ByteBuffer</div>
      case KIND.DATE => <div>java.util.Date</div>
      case KIND.BIGDECIMAL => <div>java.math.BigDecimal</div>
      case KIND.MAP =>
        return {<div>java.util.Map{lt}{toJavaDataType(dataType.getKeyType())}, {toJavaDataType(dataType.getValueType())}{gt}</div>}
      case KIND.LIST =>
        return {<div>java.util.List{lt}{toJavaDataType(dataType.getValueType())}{gt}</div>}
      case KIND.SET =>
        return {<div>java.util.Set{lt}{toJavaDataType(dataType.getValueType())}{gt}</div>}
      case KIND.ENUM =>
        val ref = dataType.getQualifiedName();
        return {<div>{ref}</div>}
      case KIND.STRUCT =>
        val ref = dataType.getQualifiedName();
        return {<div>{ref}</div>}
    }
  }


  def getJavaReadElement(dataType: DataType, index: Int):Elem = {
    dataType.kind match {
      case KIND.BOOLEAN => <div>boolean elem{index} = iprot.readBool();</div>
      case KIND.STRING => <div>String elem{index} = iprot.readString();</div>
      case KIND.BYTE => <div>byte elem{index} = iprot.readByte();</div>
      case KIND.SHORT =>  <div>short elem{index} = iprot.readI16();</div>
      case KIND.INTEGER => <div> int elem{index} = iprot.readI32();</div>
      case KIND.LONG => <div>long elem{index} = iprot.readI64();</div>
      case KIND.DOUBLE => <div> double elem{index} = iprot.readDouble();</div>
      case KIND.BINARY => <div>java.nio.ByteBuffer elem{index} = iprot.readBinary();</div>
      case KIND.BIGDECIMAL => <div>java.math.BigDecimal elem{index} = new java.math.BigDecimal(iprot.readString());</div>
      case KIND.DATE => <div>Long time = iprot.readI64(); java.util.Date elem{index} = new java.util.Date(time);</div>
      case KIND.STRUCT => <div>{dataType.qualifiedName} elem{index} = new {dataType.qualifiedName}();
        new {dataType.qualifiedName.substring(dataType.qualifiedName.lastIndexOf(".")+1)}Serializer().read(elem{index}, iprot);</div>
      case KIND.ENUM => <div>{dataType.qualifiedName} elem{index} = {dataType.qualifiedName}.findByValue(iprot.readI32());</div>
      case KIND.MAP => <div>com.isuwang.org.apache.thrift.protocol.TMap _map{index} = iprot.readMapBegin();
        java.util.Map{lt}{toJavaDataType(dataType.keyType)},{toJavaDataType(dataType.valueType)}{gt} elem{index} = new java.util.HashMap{lt}{gt}(_map{index}.size);
        for(int _i{index} = 0; _i{index} {lt} _map{index}.size; ++ _i{index})<block>
          {getJavaReadElement(dataType.keyType, index+1)}
          {getJavaReadElement(dataType.valueType, index+2)}
          elem{index}.put(elem{index+1}, elem{index+2});
        </block>
        iprot.readMapEnd();</div>
      case KIND.LIST => <div> com.isuwang.org.apache.thrift.protocol.TList _list{index} = iprot.readListBegin();
        java.util.List{lt}{toJavaDataType(dataType.valueType)}{gt} elem{index} = new java.util.ArrayList{lt}{gt}(_list{index}.size);
        for(int _i{index} = 0; _i{index} {lt} _list{index}.size; ++ _i{index})<block>
          {getJavaReadElement(dataType.valueType, index+1)}
          elem{index}.add(elem{index+1});
        </block>
        iprot.readListEnd();</div>
      case KIND.SET => <div>com.isuwang.org.apache.thrift.protocol.TSet _set{index} = iprot.readSetBegin();
        java.util.Set{lt}{toJavaDataType(dataType.valueType)}{gt} elem{index} = new java.util.HashSet{lt}{gt}(_set{index}.size);
        for(int _i{index} = 0; _i{index} {lt} _set{index}.size; ++ _i{index})<block>
          {getJavaReadElement(dataType.valueType, index+1)}
          elem{index}.add(elem{index+1});
        </block>
        iprot.readSetEnd();
      </div>
      case _ => <div></div>
    }
  }

  def getJavaSetElement(field: Field): Elem = {
    field.dataType.kind match {
      case KIND.VOID => <div>com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);</div>
      case _ => <div> bean.set{field.name.charAt(0).toUpper + field.name.substring(1)}({if(field.optional) <div>Optional.of(</div>}elem0{if(field.optional) <div>)</div>});</div>
    }

  }

  def getJavaReadAndSetElement(field: Field):Elem = {
    <div>{getJavaReadElement(field.getDataType, 0)}
         {getJavaSetElement(field)}</div>
  }

  def getReadMethod(struct: Struct): Elem = {
    <div>
      @Override
      public void read({toStructName(struct)} bean, TProtocol iprot) throws TException<block>

        com.isuwang.org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();

        while(true)<block>
          schemeField = iprot.readFieldBegin();
          if(schemeField.type == com.isuwang.org.apache.thrift.protocol.TType.STOP)<block> break;</block>

          switch(schemeField.id)<block>
          {
            toFieldArrayBuffer(struct.getFields).map{(field : Field) =>{
              <div>
              case {field.tag}:
                if(schemeField.type == {toThriftDateType(field.dataType)})<block>
                {getJavaReadAndSetElement(field)}
                </block>else<block>
                     com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                </block>
                break;
              </div>
            }}
          }
            <div>
                default:
                  com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            </div>
          </block>
          iprot.readFieldEnd();
        </block>
        iprot.readStructEnd();

        validate(bean);
      </block>
    </div>
  }

  def toJavaWriteElement(dataType: DataType, index: Int): Elem = {

    dataType.kind match {
      case KIND.BOOLEAN => <div>oprot.writeBool(elem{index});</div>
      case KIND.BYTE => <div>oprot.writeByte(elem{index});</div>
      case KIND.SHORT => <div>oprot.writeI16(elem{index});</div>
      case KIND.INTEGER => <div>oprot.writeI32(elem{index});</div>
      case KIND.LONG => <div>oprot.writeI64(elem{index});</div>
      case KIND.DOUBLE => <div>oprot.writeDouble(elem{index});</div>
      case KIND.STRING => <div>oprot.writeString(elem{index});</div>
      case KIND.ENUM => <div>oprot.writeI32(elem{index}.getValue());</div>
      case KIND.BINARY => <div>oprot.writeBinary(elem{index});</div>
      case KIND.DATE => <div>oprot.writeI64(elem{index}.getTime());</div>
      case KIND.BIGDECIMAL => <div>oprot.writeString(elem{index}.toString());</div>
      case KIND.STRUCT => <div> new {dataType.qualifiedName.substring(dataType.qualifiedName.lastIndexOf(".")+1)}Serializer().write(elem{index}, oprot);</div>
      case KIND.LIST =>
        <div>
          oprot.writeListBegin(new com.isuwang.org.apache.thrift.protocol.TList({toThriftDateType(dataType.valueType)}, elem{index}.size()));
          for({toJavaDataType(dataType.valueType)} elem{index+1} : elem{index})<block>
          {toJavaWriteElement(dataType.valueType, index+1)}
        </block>
          oprot.writeListEnd();
        </div>
      case KIND.MAP =>
        <div>
          oprot.writeMapBegin(new com.isuwang.org.apache.thrift.protocol.TMap({toThriftDateType(dataType.keyType)}, {toThriftDateType(dataType.valueType)}, elem{index}.size()));
          for(java.util.Map.Entry{lt}{toJavaDataType(dataType.keyType)}, {toJavaDataType(dataType.valueType)}{gt} _it{index} : elem{index}.entrySet())<block>

          {toJavaDataType(dataType.keyType)} elem{index+1} = _it{index}.getKey();
          {toJavaDataType(dataType.valueType)} elem{index+2} = _it{index}.getValue();
          {toJavaWriteElement(dataType.keyType, index+1)}
          {toJavaWriteElement(dataType.valueType, index+2)}
        </block>
          oprot.writeMapEnd();
        </div>

      case KIND.SET =>
        return{
          <div>
            oprot.writeSetBegin(new com.isuwang.org.apache.thrift.protocol.TSet({toThriftDateType(dataType.valueType)}, item{index}.size()));
            for({toJavaDataType(dataType.valueType)} elem{index+1} : elem{index})<block>
            {toJavaWriteElement(dataType.valueType, index+1)}
          </block>
            oprot.writeSetEnd();
          </div>}
      case KIND.VOID => <div></div>
      case _ => <div></div>
    }
  }


  def getWriteMethod(struct: Struct): Elem = {

    var index = 0
    <div>
      @Override
      public void write({toStructName(struct)} bean, TProtocol oprot) throws TException<block>

      validate(bean);
      oprot.writeStructBegin(new com.isuwang.org.apache.thrift.protocol.TStruct("{struct.name}"));

      {toFieldArrayBuffer(struct.fields).map{(field : Field) =>{
        if(field.dataType.getKind() == DataType.KIND.VOID) {
        } else {
          if(field.isOptional){
            <div>if(bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}().isPresent())<block>
              oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("{field.name}", {toThriftDateType(field.dataType)}, (short) {field.tag}));
              {toJavaDataType(field.dataType)} elem{index} = bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}().get();
              {toJavaWriteElement(field.dataType, index)}
              {index = index + 1}
            </block>
            </div>
          }else{<div>
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("{field.name}", {toThriftDateType(field.dataType)}, (short) {field.tag}));
            {toJavaDataType(field.dataType)} elem{index} = bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}();
            {toJavaWriteElement(field.dataType, index)}
            {index = index + 1}
            oprot.writeFieldEnd();
          </div>
          }
        }
      }
      }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    </block>
    </div>
  }

  def getValidateMethod(struct: Struct) : Elem = {
    <div>
      public void validate({toStructName(struct)} bean) throws TException<block>
      {
      toFieldArrayBuffer(struct.fields).map{(field : Field) =>{
        <div>{
          if(!field.isOptional && field.dataType.kind != DataType.KIND.VOID){
            <div>
              if(bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}() == null)
              throw new SoaException(SoaBaseCode.NotNull, "{field.name}字段不允许为空");
            </div>}}</div>
          <div>{
            if(!field.isOptional && field.dataType.kind == KIND.STRUCT && field.dataType.kind != DataType.KIND.VOID){
              <div>
                if(bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}() != null)
                new {field.dataType.qualifiedName.substring(field.dataType.qualifiedName.lastIndexOf(".")+1)}Serializer().validate(bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}());
              </div>}}</div>
          <div>{
            if(field.isOptional && field.dataType.kind == KIND.STRUCT && field.dataType.kind != DataType.KIND.VOID){
              <div>
                if(bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}().isPresent())
                new {field.dataType.qualifiedName.substring(field.dataType.qualifiedName.lastIndexOf(".")+1)}Serializer().validate(bean.get{field.name.charAt(0).toUpper + field.name.substring(1)}().get());
              </div>}}</div>
      }
      }
      }
    </block>
    </div>
  }

  def getToStringElement(field: Field): Elem = {
    <div>
      stringBuilder.append("\"").append("{field.name}").append("\":{if(field.dataType.kind == DataType.KIND.STRING) <div>\"</div>}").append({getToStringByDataType(field)}).append("{if(field.dataType.kind == DataType.KIND.STRING) <div>\"</div>},");
    </div>
  }

  def getToStringByDataType(field: Field):Elem = {
    if(field.dataType.kind == KIND.STRUCT) <div>this.{field.name} == null ? "null" : this.{field.name}.toString()</div> else <div>{field.name}</div>
  }

}