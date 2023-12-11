package edu.uga.miage.m1.polygons.gui;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;

import edu.uga.miage.m1.polygons.gui.command.Command;
import edu.uga.miage.m1.polygons.gui.command.CommandMove;
import edu.uga.miage.m1.polygons.gui.command.CommandShape;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

import java.util.logging.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 * 
 * @author <a href=
 *         "mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public class JDrawingFrame extends JFrame
        implements MouseListener, MouseMotionListener {
    private enum Shapes {
        SQUARE, TRIANGLE, CIRCLE
    }

    private static final long serialVersionUID = 1L;
    private JToolBar mToolbar;
    private Shapes mSelected;
    private JPanel mPanel;
    private JLabel mLabel;
    private JButton mExport;
    private JButton mImport;
    private JButton group;
    private JPopupMenu popupMenuExport;
    private JPopupMenu popupMenuImport;
    private JMenuItem exportJsonButton;
    private JMenuItem exportXmlButton;
    private JMenuItem importJsonButton;
    private String groupement = "Grouper";
    private String exportJSON = "Export JSON";
    private String exportXML = "Export XML";
    private String export = "Export";
    private String importString = "Import";
    private String importJson = "Import Json";
    private transient List<SimpleShape> shapesList;
    private transient List<Command> commandList;
    private transient ActionListener mReusableActionListener = new ShapeActionListener();
    private transient ActionListener mSelecteurActionListener = new SelecteurActionListener();

    private int startX;
    private int startY;

    private transient Command commandMove;

    private transient SimpleShape selectedShape = null;

    /**
     * Tracks buttons to manage the background.
     */
    private EnumMap<Shapes, JButton> mButtons = new EnumMap<>(Shapes.class);

    /**
     * Default constructor that populates the main window.
     * 
     * @param frameName
     **/
    public JDrawingFrame(String frameName) {
        super(frameName);

        // Instantiates components
        mToolbar = new JToolBar("Toolbar");
        mPanel = new DrawingPanel();
        mPanel.setBackground(Color.WHITE);
        mPanel.setLayout(null);
        mPanel.setMinimumSize(new Dimension(400, 400));
        mPanel.addMouseListener(this);
        mPanel.addMouseMotionListener(this);
        mLabel = new JLabel(" ", javax.swing.SwingConstants.LEFT);
        mExport = new JButton(export);
        mImport = new JButton(importString);
        popupMenuExport = new JPopupMenu();
        popupMenuImport = new JPopupMenu();
        exportJsonButton = new JMenuItem(exportJSON);
        exportXmlButton = new JMenuItem(exportXML);
        importJsonButton = new JMenuItem(importJson);
        group = new JButton(groupement);
        group.setActionCommand(groupement);
        exportJsonButton.addActionListener(mReusableActionListener);
        exportXmlButton.addActionListener(mReusableActionListener);
        importJsonButton.addActionListener(mReusableActionListener);
        shapesList = new ArrayList<>();
        commandList = new ArrayList<>();

        // Action Listener pour main Button export
        mExport.addActionListener(e -> popupMenuExport.show(mExport, 0, mExport.getHeight()));

        // Action Listener pour main button import
        mImport.addActionListener(e -> popupMenuImport.show(mImport, 0, mImport.getHeight()));

        // Add to popupMenu
        popupMenuExport.add(exportJsonButton);
        popupMenuExport.add(exportXmlButton);
        popupMenuImport.add(importJsonButton);

        // Fills the panel
        JPanel newPanel = new DrawingPanel();
        newPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, -155, 0, -55);
        newPanel.add(mExport, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        newPanel.add(mImport, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        newPanel.add(mToolbar, gbc);

        setLayout(new BorderLayout());
        add(newPanel, BorderLayout.NORTH);
        add(mPanel, BorderLayout.CENTER);
        add(mLabel, BorderLayout.SOUTH);

        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(getClass().getResource("images/square.png")));
        addShape(Shapes.TRIANGLE, new ImageIcon(getClass().getResource("images/triangle.png")));
        addShape(Shapes.CIRCLE, new ImageIcon(getClass().getResource("images/circle.png")));
        addSelecteur(new ImageIcon(getClass().getResource("images/le-curseur.png")));

        setPreferredSize(new Dimension(400, 400));

        // Ajoute un gestionnaire d'evenements de clavier pur "Ctr-Z"
        InputMap inputMap = mPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mPanel.getActionMap();

        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(ctrlZ, "commandList.get(commandList.size() - 1).undo()");
        actionMap.put("commandList.get(commandList.size() - 1).undo()", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!commandList.isEmpty()) {
                    commandList.get(commandList.size() - 1).undo();
                    commandList.remove(commandList.size() - 1);
                    mPanel.repaint();
                }
            }
        });
    }

    public JPanel getMPanel() {
        return this.mPanel;
    }

    public List<SimpleShape> getShapesList() {
        return this.shapesList;
    }

    /**
     * Injects an available <tt>SimpleShape</tt> into the drawing frame.
     * 
     * @param name The name of the injected <tt>SimpleShape</tt>.
     * @param icon The icon associated with the injected <tt>SimpleShape</tt>.
     **/
    private void addShape(Shapes shape, ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        mButtons.put(shape, button);
        button.setActionCommand(shape.toString());
        button.addActionListener(mReusableActionListener);

        if (mSelected == null) {
            button.doClick();
        }

        mToolbar.add(button);
        mToolbar.validate();
        repaint();
    }

    private void addSelecteur(ImageIcon icon) {
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(53, 53, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        button.setActionCommand("curseur");
        button.addActionListener(mSelecteurActionListener);

        if (mSelected == null) {
            button.doClick();
        }

        mToolbar.add(button);
        mToolbar.validate();
        repaint();
    }

    public void mouseClicked(MouseEvent evt) {
        Logger msg = Logger.getLogger("Error");
        if (mPanel.contains(evt.getX(), evt.getY())
                && !((SelecteurActionListener) mSelecteurActionListener).isCursorSelected()) {
            switch (mSelected) {
                case CIRCLE:
                    Circle circle = new Circle(evt.getX(), evt.getY());
                    shapesList.add(circle);
                    Command commandCircle = new CommandShape(circle, this);
                    commandCircle.execute();
                    commandList.add(commandCircle);
                    break;
                case TRIANGLE:
                    Triangle triangle = new Triangle(evt.getX(), evt.getY());
                    shapesList.add(triangle);
                    Command commandTriangle = new CommandShape(triangle, this);
                    commandTriangle.execute();
                    commandList.add(commandTriangle);
                    break;
                case SQUARE:
                    Square square = new Square(evt.getX(), evt.getY());
                    shapesList.add(square);
                    Command commandSquare = new CommandShape(square, this);
                    commandSquare.execute();
                    commandList.add(commandSquare);
                    break;
                default:
                    msg.log(Level.INFO, "No shape named {0}", mSelected);

            }
        }
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * 
     * @param evt The associated mouse event.
     **/
    public void mouseEntered(MouseEvent evt) {
        // This method is intentionally left empty because we do not need to perform any
        // action
        // when the mouse enters the component. If functionality is needed, it should be
        // implemented here.
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * 
     * @param evt The associated mouse event.
     **/
    public void mouseExited(MouseEvent evt) {
        mLabel.setText(" ");
        mLabel.repaint();
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     * 
     * @param evt The associated mouse event.
     **/
    public void mousePressed(MouseEvent evt) {
        startX = evt.getX();
        startY = evt.getY();

        for (SimpleShape shape : shapesList) {
            if (((shape.getX() <= startX + 40) && (shape.getX() >= startX - 40))
                    && ((shape.getY() <= startY + 30) && (shape.getY() >= startY - 30))) {
                selectedShape = shape;
                commandMove = new CommandMove(selectedShape);
                break;

            }
        }

    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     * 
     * @param evt The associated mouse event.
     **/
    public void mouseReleased(MouseEvent evt) {
        Graphics2D g = (Graphics2D) mPanel.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, mPanel.getWidth(), mPanel.getHeight());
        shapesList.forEach(shape -> shape.draw(g));
        if (selectedShape != null) {
            commandList.add(commandMove);
        }

    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     * 
     * @param evt The associated mouse event.
     **/
    public void mouseDragged(MouseEvent evt) {
        // Implement the logic to handle mouse dragging events here
        // For example, you can add code to respond to dragging gestures.
        if ((((SelecteurActionListener) mSelecteurActionListener).isCursorSelected()) && selectedShape != null) {
            int newX = evt.getX();
            int newY = evt.getY();

            ((CommandMove) commandMove).setLocation(newX, newY);
            commandMove.execute();

            startX = newX + 25;
            startY = newY + 25;
        }

    }

    /**
     * Implements an empty method for the <tt>MouseMotionListener</tt>
     * interface.
     * 
     * @param evt The associated mouse event.
     **/
    public void mouseMoved(MouseEvent evt) {
        modifyLabel(evt);
    }

    private void modifyLabel(MouseEvent evt) {
        mLabel.setText("(" + evt.getX() + "," + evt.getY() + ")");
    }

    /**
     * Simple action listener for shape tool bar buttons that sets
     * the drawing frame's currently selected shape when receiving
     * an action event
     **/

    class ShapeActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            ((SelecteurActionListener) mSelecteurActionListener).setIsCursedSelected(false);
            Logger msg = Logger.getLogger("Error");
            if (evt.getActionCommand().equals(exportXML)) {
                File file = new File("export.xml");
                try (FileWriter fileWriter = new FileWriter(file);) {
                    String xmlShapes = exportShapesToXml();
                    fileWriter.write(xmlShapes);
                } catch (IOException | ParserConfigurationException e) {
                    msg.log(Level.WARNING, "erreur dans l''export xml", e);
                }
                JOptionPane.showMessageDialog(null, "Export Xml reussie !");
            } else if (evt.getActionCommand().equals(exportJSON)) {

                File file = new File("export.json");
                try (FileWriter fileWriter = new FileWriter(file);) {
                    String jsonShapes = exportShapesToJson();
                    fileWriter.write(jsonShapes);
                } catch (IOException e) {
                    msg.log(Level.WARNING, "Erreur dans l''export JSON", e);
                }
                JOptionPane.showMessageDialog(null, "Export Json reussie !");
            } else if (evt.getActionCommand().equals(importJson)) {
                actionPerformesImport();
            }
            // Itère sur tous les boutons
            Iterator<Shapes> keys = mButtons.keySet().iterator();
            while (keys.hasNext()) {
                Shapes shape = keys.next();
                JButton btn = mButtons.get(shape);
                if (evt.getActionCommand().equals(shape.toString())) {
                    btn.setBorderPainted(true);
                    mSelected = shape;
                } else {
                    btn.setBorderPainted(false);
                }
                btn.repaint();
            }
        }

        private void actionPerformesImport() {
            JFileChooser fileChooser = new JFileChooser();

            int returnValue = fileChooser.showOpenDialog(JDrawingFrame.this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                String fileName = selectedFile.getName();
                if (fileName.endsWith(".json")) {
                    JOptionPane.showMessageDialog(null, "Fichier Jsosn selectionne : " + selectedFile);
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));) {
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            content.append(line);
                        }

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(content.toString());
                        JSONObject jsonObject = (JSONObject) obj;

                        JSONArray shapesArray = (JSONArray) jsonObject.get("shapes");

                        for (Object shapeObj : shapesArray) {
                            JSONObject shapeJson = (JSONObject) shapeObj;
                            String type = (String) shapeJson.get("type");
                            Long x = (Long) shapeJson.get("x");
                            Long y = (Long) shapeJson.get("y");

                            createForme(type, x, y);

                        }
                        mPanel.repaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Type de fichier non pris en charge");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aucun fichier selectione");
            }
        }

        private void createForme(String type, Long x, Long y) {
            if (type.equals("circle")) {
                Circle circle = new Circle(x.intValue() + 25, y.intValue() + 25);
                shapesList.add(circle);
            } else if (type.equals("triangle")) {
                Triangle triangle = new Triangle(x.intValue() + 25, y.intValue() + 25);
                shapesList.add(triangle);
            } else if (type.equals("square")) {
                Square square = new Square(x.intValue() + 25, y.intValue() + 25);
                shapesList.add(square);
            }
        }

        private String exportShapesToJson() {
            JSonVisitor jSonVisitor = new JSonVisitor();
            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\n\"shapes\":[\n");
            int index = 0;
            for (SimpleShape shape : shapesList) {
                ((Visitable) shape).accept(jSonVisitor);

                jsonResult.append(jSonVisitor.getRepresentation());
                if (index != shapesList.size() - 1) {
                    jsonResult.append(",").append("\n");
                } else {
                    jsonResult.append("\n");
                }
                jSonVisitor.reset();
                index++;
            }
            jsonResult.append("]\n}");
            return jsonResult.toString();
        }

        private String exportShapesToXml() throws ParserConfigurationException {
            XMLVisitor xmlVisitor = new XMLVisitor();
            StringBuilder xmlResult = new StringBuilder();
            xmlResult.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlResult.append("<root>\n<shapes>\n");
            for (SimpleShape shape : shapesList) {
                ((Visitable) shape).accept(xmlVisitor);
                xmlResult.append(xmlVisitor.getRepresentation()).append("\n");
                xmlVisitor.reset();
            }
            xmlResult.append("</shapes>\n</root>");
            return xmlResult.toString();
        }
    }

    class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (SimpleShape shape : shapesList) {
                if (shape instanceof SimpleShape) {
                    shape.draw((Graphics2D) g);
                }
            }
        }
    }

    class SelecteurActionListener implements ActionListener {
        private boolean isCursorSelected = false;

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getActionCommand().equals("curseur")) {
                isCursorSelected = true;
            } else {
                isCursorSelected = false;
            }
        }

        public boolean isCursorSelected() {
            return isCursorSelected;
        }

        public void setIsCursedSelected(boolean selected) {
            this.isCursorSelected = selected;
        }
    }

}
