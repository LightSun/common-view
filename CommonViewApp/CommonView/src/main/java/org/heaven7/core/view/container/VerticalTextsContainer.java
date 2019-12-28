package org.heaven7.core.view.container;

import org.heaven7.core.view.container.factory.TextContainerFactory;

import java.util.List;

public class VerticalTextsContainer extends VerticalContainer {

   /* private TextContainerFactory textContainerFactory;
    private int layoutId;
    private int textViewId;
    private List<? extends CharSequence> texts;*/

    public VerticalTextsContainer(VerticalTextsContainer.Builder builder) {
        for (CharSequence cs: builder.texts){
            TextContainerFactory factory = builder.textContainerFactory;
            TextContainer container = factory != null ? factory.createTextContainer() : new TextContainer();
            container.setLayoutId(builder.layoutId);
            container.setTextViewId(builder.textViewId);
            container.setText(cs);
            addContainer(container);
        }
    }
    public VerticalTextsContainer(int layoutId, int textViewId, List<? extends CharSequence> texts) {
        this(new Builder()
                .setLayoutId(layoutId)
                .setTextViewId(textViewId)
                .setTexts(texts)
        );
    }
    public VerticalTextsContainer(int layoutId, List<? extends CharSequence> texts) {
        this(layoutId, 0, texts);
    }

    public static class Builder {
        private TextContainerFactory textContainerFactory;
        private int layoutId;
        private int textViewId;
        private List<? extends CharSequence> texts;

        public Builder setTextContainerFactory(TextContainerFactory textContainerFactory) {
            this.textContainerFactory = textContainerFactory;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setTextViewId(int textViewId) {
            this.textViewId = textViewId;
            return this;
        }

        public Builder setTexts(List<? extends CharSequence> texts) {
            this.texts = texts;
            return this;
        }

        public VerticalTextsContainer build() {
            return new VerticalTextsContainer(this);
        }
    }
}
