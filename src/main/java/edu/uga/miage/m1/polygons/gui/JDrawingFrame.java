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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    public JPanel mPanel;
    private JLabel mLabel;
    private JButton mExportJSON;
    private JButton mExportXML;
    private String exportJSON = "Export JSON";
    private String exportXML = "Export XML";
    public transient List<SimpleShape> shapesList;
    private transient List<Command> commandList;
    private transient ActionListener mReusableActionListener = new ShapeActionListener();
    private transient ActionListener mSelecteurActionListener = new SelecteurActionListener();

    private int startX;
    private int startY;

    Command commandMove;

    private transient SimpleShape selectedShape = null;

    private Set<Command> addedCommands = new HashSet<>();

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
        mExportJSON = new JButton(exportJSON);
        mExportXML = new JButton(exportXML);
        mExportJSON.setActionCommand(exportJSON);
        mExportXML.setActionCommand(exportXML);
        mExportJSON.addActionListener(mReusableActionListener);
        mExportXML.addActionListener(mReusableActionListener);
        shapesList = new ArrayList<>();
        commandList = new ArrayList<>();

        // Fills the panel
        setLayout(new BorderLayout());
        add(mToolbar, BorderLayout.NORTH);
        add(mPanel, BorderLayout.CENTER);
        add(mLabel, BorderLayout.SOUTH);

        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(getClass().getResource("images/square.png")));
        addShape(Shapes.TRIANGLE, new ImageIcon(getClass().getResource("images/triangle.png")));
        addShape(Shapes.CIRCLE, new ImageIcon(getClass().getResource("images/circle.png")));
        addSelecteur(new ImageIcon(getClass().getResource("images/le-curseur.png")));
        mToolbar.add(exportJSON, mExportJSON);
        mToolbar.add(exportXML, mExportXML);

        setPreferredSize(new Dimension(400, 400));

        // Ajoute un gestionnaire d'evenements de clavier pur "Ctr-Z"
        InputMap inputMap = mPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mPanel.getActionMap();

        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(ctrlZ, "commandList.get(commandList.size() - 1).undo()");
        actionMap.put("commandList.get(commandList.size() - 1).undo()", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("size command avant undo : " + commandList.size());
                commandList.get(commandList.size() - 1).undo();
                commandList.remove(commandList.size() - 1);
                System.out.println("size command apres undo : " + commandList.size());
                mPanel.repaint();
            }
        });
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
                System.out.println("trouve !");
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
            // commandList.add(commandMove);

            startX = newX;
            startY = newY;
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
            } else if (evt.getActionCommand().equals(exportJSON)) {

                File file = new File("export.json");
                try (FileWriter fileWriter = new FileWriter(file);) {
                    String jsonShapes = exportShapesToJson();
                    fileWriter.write(jsonShapes);
                } catch (IOException e) {
                    msg.log(Level.WARNING, "Erreur dans l''export JSON", e);
                }
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

        private String exportShapesToJson() {
            JSonVisitor jSonVisitor = new JSonVisitor();
            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\n\"shapes\":[\n");
            for (SimpleShape shape : shapesList) {
                ((Visitable) shape).accept(jSonVisitor);

                jsonResult.append(jSonVisitor.getRepresentation()).append("\n");
                jSonVisitor.reset();
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
