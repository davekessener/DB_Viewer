package viewer.tools.table.edit;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class EditCell<T> extends TableCell<T, String>
{
    private TextField text_;

    @Override
    public void startEdit()
    {
        if(!isEmpty())
        {
            super.startEdit();
            
            createTextField();
            setText(null);
            setGraphic(text_);
        }
        
        text_.selectAll();
    }

    @Override
    public void cancelEdit()
    {
        super.cancelEdit();

        setText(getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty)
    {
        super.updateItem(item, empty);

        if(empty)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            if(isEditing())
            {
                if(text_ != null)
                {
                    text_.setText(getString());
                }
                
                setText(null);
                setGraphic(text_);
            }
            else
            {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField()
    {
        text_ = new TextField(getString());
        text_.setMinWidth(getWidth() - getGraphicTextGap() * 2);
        text_.focusedProperty().addListener((o, ov, nv) ->
        {
            if(!nv)
            {
                commitEdit(text_.getText());
            }
        });
    }

    private String getString()
    {
        return getItem() == null ? "" : getItem().toString();
    }
}
