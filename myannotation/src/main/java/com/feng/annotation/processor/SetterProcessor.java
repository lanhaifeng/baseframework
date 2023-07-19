package com.feng.annotation.processor;

import com.feng.annotation.Setter;
import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Setter插入注解支持
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/1/4 18:17创建:SetterProcessor
 * @since v2.0.0
 */
@SupportedAnnotationTypes("com.feng.annotation.Setter")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedOptions({"debug", "verify"})
@AutoService(Processor.class)
public class SetterProcessor extends AbstractProcessor {
    private Messager messager;

    private JavacTrees trees;

    private TreeMaker treeMaker;

    private Names names;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Setter.class);
        System.out.println("test setter");

        set.forEach(element -> {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                        }
                    }
                    jcVariableDeclList.forEach(jcVariableDecl -> {
                        messager.printMessage(Diagnostic.Kind.NOTE, "set " + jcVariableDecl.getName() + " has been processed");
                        treeMaker.pos = jcVariableDecl.pos;
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeSetterMethodDecl(jcVariableDecl));

                    });
                    super.visitClassDef(jcClassDecl);
                }

            });
        });
        return true;
    }

    private JCTree.JCMethodDecl makeSetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(
                treeMaker.Exec(
                        treeMaker.Assign(
                                treeMaker.Select(
                                        treeMaker.Ident(names.fromString("this")),
                                        names.fromString(jcVariableDecl.name.toString())
                                ),
                                treeMaker.Ident(names.fromString(jcVariableDecl.name.toString()))
                        )
                )
        );

        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());

        // 生成入参
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), jcVariableDecl.getName(),jcVariableDecl.vartype, null);

        List<JCTree.JCVariableDecl> paramList = List.of(param);

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC), // 方法限定值
                setNewMethodName(jcVariableDecl.getName()), // 方法名
                treeMaker.Type(new Type.JCVoidType()), // 返回类型
                List.nil(),
                paramList, // 入参
                List.nil(),
                body,
                null
        );

    }

    private Name setNewMethodName(Name name) {
        String s = name.toString();
        return names.fromString("set" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }
}
