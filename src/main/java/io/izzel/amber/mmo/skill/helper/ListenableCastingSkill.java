package io.izzel.amber.mmo.skill.helper;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.SkillSubject;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.skills.operations.MultiOperation;
import io.izzel.amber.mmo.skill.skills.operations.OperationEnd;
import io.izzel.amber.mmo.skill.skills.operations.OperationStart;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ListenableCastingSkill<E extends EntitySkill<?, ?>> extends ReflectiveCastingSkill<E> {

    private final Multimap<Predicate<Class<?>>, BiFunction<SkillOperation, SkillSubject.CastOperator, Void>> map = MultimapBuilder.hashKeys().arrayListValues().build();

    public ListenableCastingSkill() {
        for (Map.Entry<Predicate<Class<?>>, Method> entry : Capture.captureMethods(this.getClass()).entrySet()) {
            map.put(entry.getKey(), (o, c) -> {
                try {
                    entry.getValue().invoke(this, o, c);
                    return null;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        }
    }

    @Override
    protected final void perform(SkillOperation operation, SkillSubject.CastOperator operator) throws UnsupportedOperationException {
        if (operation instanceof MultiOperation) {
            for (SkillOperation op : ((MultiOperation<?>) operation).getOperations()) {
                perform(op, operator);
            }
            return;
        }
        Class<? extends SkillOperation> operationClass = operation.getClass();
        for (Map.Entry<Predicate<Class<?>>, BiFunction<SkillOperation, SkillSubject.CastOperator, Void>> entry : map.entries()) {
            if (entry.getKey().test(operationClass)) {
                entry.getValue().apply(operation, operator);
            }
        }
    }

    @Operation
    private void internalStart(OperationStart start, SkillSubject.CastOperator operator) {
        operator.add(this);
    }

    @Operation
    private void internalEnd(OperationEnd start, SkillSubject.CastOperator operator) {
        operator.remove(this);
    }

}
