package de.kontext_e.jqassistant.plugin.ruby.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.*;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class AddDescriptorVisitor implements NodeVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddDescriptorVisitor.class);

    private final RubyFileDescriptor rubyFileDescriptor;
    private final Store store;
    private Map<String, ModuleDescriptor> fqnToModule = new HashMap<>();
    private Map<String, ClassDescriptor> fqnToClass = new HashMap<>();

    AddDescriptorVisitor(RubyFileDescriptor rubyFileDescriptor, Store store) {
        this.rubyFileDescriptor = rubyFileDescriptor;
        this.store = store;
    }

    @Override
    public Object visitAliasNode(AliasNode iVisited) {
        return null;
    }

    @Override
    public Object visitAndNode(AndNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgsNode(ArgsNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgsCatNode(ArgsCatNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgsPushNode(ArgsPushNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgumentNode(ArgumentNode iVisited) {
        return null;
    }

    @Override
    public Object visitArrayNode(ArrayNode iVisited) {
        return null;
    }

    @Override
    public Object visitAttrAssignNode(AttrAssignNode iVisited) {
        return null;
    }

    @Override
    public Object visitBackRefNode(BackRefNode iVisited) {
        return null;
    }

    @Override
    public Object visitBeginNode(BeginNode iVisited) {
        return null;
    }

    @Override
    public Object visitBignumNode(BignumNode iVisited) {
        return null;
    }

    @Override
    public Object visitBlockArgNode(BlockArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitBlockArg18Node(BlockArg18Node iVisited) {
        return null;
    }

    @Override
    public Object visitBlockNode(BlockNode iVisited) {
        return null;
    }

    @Override
    public Object visitBlockPassNode(BlockPassNode iVisited) {
        return null;
    }

    @Override
    public Object visitBreakNode(BreakNode iVisited) {
        return null;
    }

    @Override
    public Object visitConstDeclNode(ConstDeclNode iVisited) {
        final ConstantDescriptor constantDescriptor = store.create(ConstantDescriptor.class);
        constantDescriptor.setName(iVisited.getName());
        final String fqn = getFqn(findParentIScopingNode(iVisited));

        if("".equals(fqn)) {
            rubyFileDescriptor.getConstants().add(constantDescriptor);
        }
        if(fqnToModule.containsKey(fqn)) {
            fqnToModule.get(fqn).getConstants().add(constantDescriptor);
        }
        if(fqnToClass.containsKey(fqn)) {
            fqnToClass.get(fqn).getConstants().add(constantDescriptor);
        }

        return null;
    }

    @Override
    public Object visitClassVarAsgnNode(ClassVarAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassVarDeclNode(ClassVarDeclNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassVarNode(ClassVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitCallNode(CallNode iVisited) {
        return null;
    }

    @Override
    public Object visitCaseNode(CaseNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassNode(ClassNode iVisited) {
        final ClassDescriptor classDescriptor = store.create(ClassDescriptor.class);
        classDescriptor.setName(iVisited.getCPath().getName());
        classDescriptor.setFullQualifiedName(getFqn(iVisited));
        fqnToClass.put(classDescriptor.getFullQualifiedName(), classDescriptor);

        final ModuleNode parentModule = findParentModule(iVisited);
        if(parentModule != null) {
            final String parentFqn = getFqn(parentModule);
            final ModuleDescriptor parentModuleDescriptor = fqnToModule.get(parentFqn);
            if(parentModuleDescriptor != null) {
                parentModuleDescriptor.getClasses().add(classDescriptor);
            }
        }

        for (MethodDefNode methodDef : iVisited.getMethodDefs()) {
            final MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
            classDescriptor.getDeclaredMethods().add(methodDescriptor);
            methodDescriptor.setName(methodDef.getName());
            methodDescriptor.setSignature(methodDef.getNormativeSignature());

            final ArgsNode args = methodDef.getArgs();
            for (Node param : args.getNormativeParameterList()) {
                if(param instanceof ArgumentNode) {
                    ArgumentNode argumentNode = (ArgumentNode) param;
                    final ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);
                    methodDescriptor.getParameters().add(parameterDescriptor);
                    parameterDescriptor.setName(argumentNode.getName());
                }
            }
        }

        final Node superNode = iVisited.getSuper();
        if(superNode instanceof ConstNode) {
            ConstNode superClass = (ConstNode) superNode;
            final String superClassName = superClass.getName();
            System.out.println("  class' super node: " + superClassName);
            // FIXME find super class within the right name space
        }


        return null;
    }

    @Override
    public Object visitCommentNode(CommentNode iVisited) {
        return null;
    }

    @Override
    public Object visitColon2Node(Colon2Node iVisited) {
        return null;
    }

    @Override
    public Object visitColon3Node(Colon3Node iVisited) {
        return null;
    }

    @Override
    public Object visitConstNode(ConstNode iVisited) {
        return null;
    }

    @Override
    public Object visitDAsgnNode(DAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitDRegxNode(DRegexpNode iVisited) {
        return null;
    }

    @Override
    public Object visitDStrNode(DStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitDSymbolNode(DSymbolNode iVisited) {
        return null;
    }

    @Override
    public Object visitDVarNode(DVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitDXStrNode(DXStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefinedNode(DefinedNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefnNode(DefnNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefsNode(DefsNode iVisited) {
        return null;
    }

    @Override
    public Object visitDotNode(DotNode iVisited) {
        return null;
    }

    @Override
    public Object visitEncodingNode(EncodingNode iVisited) {
        return null;
    }

    @Override
    public Object visitEnsureNode(EnsureNode iVisited) {
        return null;
    }

    @Override
    public Object visitEvStrNode(EvStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitFCallNode(FCallNode iVisited) {
        if("include".equals(iVisited.getName())) {
            final ClassNode parentClass = findParentClass(iVisited);
            if (iVisited.getArgs() instanceof ArrayNode) {
                ArrayNode args = (ArrayNode) iVisited.getArgs();
                for (Node childNode : args.childNodes()) {
                    if (childNode instanceof ConstNode) {
                        ConstNode constNode = (ConstNode) childNode;
                        final String fqn = getFqn(parentClass);
                        if (fqnToClass.containsKey(fqn)) {
                            final IncludeDescriptor includeDescriptor = store.create(IncludeDescriptor.class);
                            includeDescriptor.setName(constNode.getName());
                            fqnToClass.get(fqn).getIncludes().add(includeDescriptor);
                        } else {
                            LOGGER.error("No class with fqn " + fqn + " found");
                        }
                    }
                }
            }

        } else if("require".equals(iVisited.getName())) {
            if (iVisited.getArgs() instanceof ArrayNode) {
                ArrayNode args = (ArrayNode) iVisited.getArgs();
                for (Node childNode : args.childNodes()) {
                    if (childNode instanceof StrNode) {
                        StrNode strNode = (StrNode) childNode;
                        final RequireDescriptor requireDescriptor = store.create(RequireDescriptor.class);
                        requireDescriptor.setName(strNode.getValue());
                        rubyFileDescriptor.getRequires().add(requireDescriptor);
                    }
                }
            }

        } else if("attr".equals(iVisited.getName())) {
            addAttribute(iVisited);
        } else if(" attr_accessor".equals(iVisited.getName())) {
            addAttribute(iVisited);
        } else if("attr_reader".equals(iVisited.getName())) {
            addAttribute(iVisited);
        } else if("attr_writer".equals(iVisited.getName())) {
            addAttribute(iVisited);
        } else {
            final MethodDefNode parentMethod = findParentMethod(iVisited);
            if(parentMethod != null) {
                System.out.println(parentMethod+" calls "+iVisited.getName());
            } else {
                System.out.println("!!! no method calls "+iVisited.getName());
            }
        }
        return null;
    }

    private void addAttribute(FCallNode iVisited) {
        final IScopingNode parentIScopingNode = findParentIScopingNode(iVisited);
        if (iVisited.getArgs() instanceof ArrayNode) {
            ArrayNode args = (ArrayNode) iVisited.getArgs();
            for (Node childNode : args.childNodes()) {
                if (childNode instanceof SymbolNode) {
                    SymbolNode symbolNode = (SymbolNode) childNode;
                    System.out.println(" declared variable " + symbolNode.getName());
                    final AttributeDescriptor attributeDescriptor = store.create(AttributeDescriptor.class);
                    attributeDescriptor.setName(symbolNode.getName());
                    AttributedDescriptor module = findParentDescriptor(parentIScopingNode);
                    if(module != null) {
                        module.getAttributes().add(attributeDescriptor);
                    } else {
                        LOGGER.error("No module for attribute " + symbolNode.getName());
                    }
                }
            }
        }
    }

    @Override
    public Object visitFalseNode(FalseNode iVisited) {
        return null;
    }

    @Override
    public Object visitFixnumNode(FixnumNode iVisited) {
        return null;
    }

    @Override
    public Object visitFlipNode(FlipNode iVisited) {
        return null;
    }

    @Override
    public Object visitFloatNode(FloatNode iVisited) {
        return null;
    }

    @Override
    public Object visitForNode(ForNode iVisited) {
        return null;
    }

    @Override
    public Object visitGlobalAsgnNode(GlobalAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitGlobalVarNode(GlobalVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitHashNode(HashNode iVisited) {
        return null;
    }

    @Override
    public Object visitInstAsgnNode(InstAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitInstVarNode(InstVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitIfNode(IfNode iVisited) {
        return null;
    }

    @Override
    public Object visitImplicitNilNode(ImplicitNilNode visited) {
        return null;
    }

    @Override
    public Object visitIterNode(IterNode iVisited) {
        return null;
    }

    @Override
    public Object visitKeywordArgNode(KeywordArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitKeywordRestArgNode(KeywordRestArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitLambdaNode(LambdaNode visited) {
        return null;
    }

    @Override
    public Object visitListNode(ListNode iVisited) {
        return null;
    }

    @Override
    public Object visitLiteralNode(LiteralNode iVisited) {
        return null;
    }

    @Override
    public Object visitLocalAsgnNode(LocalAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitLocalVarNode(LocalVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitMultipleAsgnNode(MultipleAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitMatch2Node(Match2Node iVisited) {
        return null;
    }

    @Override
    public Object visitMatch3Node(Match3Node iVisited) {
        return null;
    }

    @Override
    public Object visitMatchNode(MatchNode iVisited) {
        return null;
    }

    @Override
    public Object visitMethodNameNode(MethodNameNode iVisited) {
        // FIXME handle methods directly defined in the ruby file outside classes or modules
        return null;
    }

    @Override
    public Object visitModuleNode(ModuleNode iVisited) {
        final ModuleDescriptor moduleDescriptor = store.create(ModuleDescriptor.class);
        moduleDescriptor.setName(iVisited.getCPath().getName());
        moduleDescriptor.setFullQualifiedName(getFqn(iVisited));
        fqnToModule.put(moduleDescriptor.getFullQualifiedName(), moduleDescriptor);

        rubyFileDescriptor.getModules().add(moduleDescriptor);
        final ModuleNode parentModule = findParentModule(iVisited);
        if(parentModule != null) {
            final String parentFqn = getFqn(parentModule);
            final ModuleDescriptor parentModuleDescriptor = fqnToModule.get(parentFqn);
            if(parentModuleDescriptor != null) {
                parentModuleDescriptor.getModules().add(moduleDescriptor);
            }
        }
        return null;
    }

    @Override
    public Object visitNewlineNode(NewlineNode iVisited) {
        return null;
    }

    @Override
    public Object visitNextNode(NextNode iVisited) {
        return null;
    }

    @Override
    public Object visitNilNode(NilNode iVisited) {
        return null;
    }

    @Override
    public Object visitNotNode(NotNode iVisited) {
        return null;
    }

    @Override
    public Object visitNthRefNode(NthRefNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpElementAsgnNode(OpElementAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpAsgnNode(OpAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpAsgnAndNode(OpAsgnAndNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpAsgnOrNode(OpAsgnOrNode iVisited) {
        return null;
    }

    @Override
    public Object visitOptArgNode(OptArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitOrNode(OrNode iVisited) {
        return null;
    }

    @Override
    public Object visitPreExeNode(PreExeNode iVisited) {
        return null;
    }

    @Override
    public Object visitPostExeNode(PostExeNode iVisited) {
        return null;
    }

    @Override
    public Object visitRedoNode(RedoNode iVisited) {
        return null;
    }

    @Override
    public Object visitRegexpNode(RegexpNode iVisited) {
        return null;
    }

    @Override
    public Object visitRescueBodyNode(RescueBodyNode iVisited) {
        return null;
    }

    @Override
    public Object visitRescueNode(RescueNode iVisited) {
        return null;
    }

    @Override
    public Object visitRestArgNode(RestArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitRetryNode(RetryNode iVisited) {
        return null;
    }

    @Override
    public Object visitReturnNode(ReturnNode iVisited) {
        return null;
    }

    @Override
    public Object visitRootNode(RootNode iVisited) {
        return null;
    }

    @Override
    public Object visitSClassNode(SClassNode iVisited) {
        return null;
    }

    @Override
    public Object visitSelfNode(SelfNode iVisited) {
        return null;
    }

    @Override
    public Object visitSplatNode(SplatNode iVisited) {
        return null;
    }

    @Override
    public Object visitStrNode(StrNode iVisited) {
        return null;
    }

    @Override
    public Object visitSuperNode(SuperNode iVisited) {
        return null;
    }

    @Override
    public Object visitSValueNode(SValueNode iVisited) {
        return null;
    }

    @Override
    public Object visitSymbolNode(SymbolNode iVisited) {
        return null;
    }

    @Override
    public Object visitSyntaxNode(SyntaxNode iVisited) {
        return null;
    }

    @Override
    public Object visitToAryNode(ToAryNode iVisited) {
        return null;
    }

    @Override
    public Object visitTrueNode(TrueNode iVisited) {
        return null;
    }

    @Override
    public Object visitUndefNode(UndefNode iVisited) {
        return null;
    }

    @Override
    public Object visitUnaryCallNode(UnaryCallNode iVisited) {
        return null;
    }

    @Override
    public Object visitUntilNode(UntilNode iVisited) {
        return null;
    }

    @Override
    public Object visitVAliasNode(VAliasNode iVisited) {
        return null;
    }

    @Override
    public Object visitVCallNode(VCallNode iVisited) {
        return null;
    }

    @Override
    public Object visitWhenNode(WhenNode iVisited) {
        return null;
    }

    @Override
    public Object visitWhileNode(WhileNode iVisited) {
        return null;
    }

    @Override
    public Object visitXStrNode(XStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitYieldNode(YieldNode iVisited) {
        return null;
    }

    @Override
    public Object visitZArrayNode(ZArrayNode iVisited) {
        return null;
    }

    @Override
    public Object visitZSuperNode(ZSuperNode iVisited) {
        return null;
    }

    private ModuleNode findParentModule(Node iVisited) {
        if(iVisited == null) return null;

        final Node parent = iVisited.getParent();
        if(parent instanceof ModuleNode) {
            return (ModuleNode) parent;
        }
        return findParentModule(iVisited.getParent());
    }

    private ClassNode findParentClass(Node iVisited) {
        if(iVisited == null) return null;

        final Node parent = iVisited.getParent();
        if(parent instanceof ClassNode) {
            return (ClassNode) parent;
        }
        return findParentClass(iVisited.getParent());
    }

    private MethodDefNode findParentMethod(Node iVisited) {
        if(iVisited == null) return null;

        final Node parent = iVisited.getParent();
        if(parent instanceof MethodDefNode) {
            return (MethodDefNode) parent;
        }
        return findParentMethod(iVisited.getParent());
    }

    private IScopingNode findParentIScopingNode(Node iVisited) {
        if(iVisited == null) return null;

        final Node parent = iVisited.getParent();
        if(parent instanceof IScopingNode) {
            return (IScopingNode) parent;
        }
        return findParentIScopingNode(iVisited.getParent());
    }

    private String getFqn(IScopingNode iVisited) {
        if(iVisited == null) return "";

        final IScopingNode parentIScopingNode = findParentIScopingNode((Node) iVisited);
        if(parentIScopingNode != null) {
            return getFqn(parentIScopingNode)+"::"+iVisited.getCPath().getName();
        } else {
            return iVisited.getCPath().getName();
        }
    }

    private <T> T findParentDescriptor(IScopingNode parentIScopingNode) {
        final String fqn = getFqn(parentIScopingNode);
        if(fqnToModule.containsKey(fqn)) {
            return (T) fqnToModule.get(fqn);
        }
        if(fqnToClass.containsKey(fqn)) {
            return (T) fqnToClass.get(fqn);
        }
        return null;
    }

}